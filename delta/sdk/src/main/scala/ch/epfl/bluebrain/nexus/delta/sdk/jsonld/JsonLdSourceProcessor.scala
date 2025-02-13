package ch.epfl.bluebrain.nexus.delta.sdk.jsonld

import ch.epfl.bluebrain.nexus.delta.kernel.Mapper
import ch.epfl.bluebrain.nexus.delta.kernel.utils.UUIDF
import ch.epfl.bluebrain.nexus.delta.rdf.IriOrBNode.{BNode, Iri}
import ch.epfl.bluebrain.nexus.delta.rdf.jsonld.api.{JsonLdApi, JsonLdOptions}
import ch.epfl.bluebrain.nexus.delta.rdf.jsonld.context.ContextValue.ContextObject
import ch.epfl.bluebrain.nexus.delta.rdf.jsonld.context.JsonLdContext.keywords
import ch.epfl.bluebrain.nexus.delta.rdf.jsonld.context.{ContextValue, RemoteContextResolution}
import ch.epfl.bluebrain.nexus.delta.rdf.jsonld.decoder.JsonLdDecoder
import ch.epfl.bluebrain.nexus.delta.rdf.jsonld.{CompactedJsonLd, ExpandedJsonLd}
import ch.epfl.bluebrain.nexus.delta.sdk.jsonld.JsonLdRejection._
import ch.epfl.bluebrain.nexus.delta.sdk.model.identities.Caller
import ch.epfl.bluebrain.nexus.delta.sdk.model.projects.Project
import ch.epfl.bluebrain.nexus.delta.sdk.model.resolvers.ResolverContextResolution
import ch.epfl.bluebrain.nexus.delta.sdk.syntax._
import io.circe.syntax._
import io.circe.{Json, JsonObject}
import monix.bio.{IO, UIO}

/**
  * Allows to define different JsonLd processorces
  */
sealed abstract class JsonLdSourceProcessor(implicit api: JsonLdApi) {

  def uuidF: UUIDF

  protected def getOrGenerateId(iri: Option[Iri], project: Project): UIO[Iri] =
    iri.fold(uuidF().map(uuid => project.base.iri / uuid.toString))(IO.pure)

  protected def expandSource(
      project: Project,
      source: Json
  )(implicit rcr: RemoteContextResolution): IO[InvalidJsonLdFormat, (ContextValue, ExpandedJsonLd)] = {
    implicit val opts: JsonLdOptions = JsonLdOptions(base = Some(project.base.iri))
    ExpandedJsonLd(source)
      .flatMap {
        case expanded if expanded.isEmpty && source.topContextValueOrEmpty.isEmpty =>
          val ctx = defaultCtx(project)
          ExpandedJsonLd(source.addContext(ctx.contextObj)).map(ctx -> _)
        case expanded                                                              =>
          UIO.pure(source.topContextValueOrEmpty -> expanded)
      }
      .mapError(err => InvalidJsonLdFormat(None, err))
  }

  protected def checkAndSetSameId(iri: Iri, expanded: ExpandedJsonLd): IO[UnexpectedId, ExpandedJsonLd] =
    expanded.rootId match {
      case _: BNode        => UIO.pure(expanded.replaceId(iri))
      case `iri`           => UIO.pure(expanded)
      case payloadIri: Iri => IO.raiseError(UnexpectedId(iri, payloadIri))
    }

  private def defaultCtx(project: Project): ContextValue =
    ContextObject(JsonObject(keywords.vocab -> project.vocab.asJson, keywords.base -> project.base.asJson))

}

object JsonLdSourceProcessor {

  /**
    * Allows to parse the given json source to JsonLD compacted and expanded using static contexts
    */
  final class JsonLdSourceParser[R](contextIri: Seq[Iri], override val uuidF: UUIDF)(implicit
      api: JsonLdApi,
      rejectionMapper: Mapper[InvalidJsonLdRejection, R]
  ) extends JsonLdSourceProcessor {

    /**
      * Converts the passed ''source'' to JsonLD compacted and expanded. The @id value is extracted from the payload.
      * When no @id is present, one is generated using the base on the project suffixed with a randomly generated UUID.
      *
      * @param project
      *   the project with the base used to generate @id when needed and the @context when not provided on the source
      * @param source
      *   the Json payload
      * @return
      *   a tuple with the resulting @id iri, the compacted Json-LD and the expanded Json-LD
      */
    def apply(
        project: Project,
        source: Json
    )(implicit rcr: RemoteContextResolution): IO[R, (Iri, CompactedJsonLd, ExpandedJsonLd)] = {
      for {
        (ctx, originalExpanded) <- expandSource(project, source.addContext(contextIri: _*))
        iri                     <- getOrGenerateId(originalExpanded.rootId.asIri, project)
        expanded                 = originalExpanded.replaceId(iri)
        compacted               <- expanded.toCompacted(ctx).mapError(err => InvalidJsonLdFormat(Some(iri), err))
      } yield (iri, compacted, expanded)
    }.mapError(rejectionMapper.to)

    /**
      * Converts the passed ''source'' to JsonLD compacted and expanded. The @id value is extracted from the payload if
      * exists and compared to the passed ''iri''. If they aren't equal an [[UnexpectedId]] rejection is issued.
      *
      * @param project
      *   the project used to generate the @context when no @context is provided on the source
      * @param source
      *   the Json payload
      * @return
      *   a tuple with the compacted Json-LD and the expanded Json-LD
      */
    def apply(
        project: Project,
        iri: Iri,
        source: Json
    )(implicit
        rcr: RemoteContextResolution
    ): IO[R, (CompactedJsonLd, ExpandedJsonLd)] = {
      for {
        (ctx, originalExpanded) <- expandSource(project, source.addContext(contextIri: _*))
        expanded                <- checkAndSetSameId(iri, originalExpanded)
        compacted               <- expanded.toCompacted(ctx).mapError(err => InvalidJsonLdFormat(Some(iri), err))
      } yield (compacted, expanded)
    }.mapError(rejectionMapper.to)

  }

  /**
    * Allows to parse the given json source to JsonLD compacted and expanded using static and resolver-based contexts
    */
  final class JsonLdSourceResolvingParser[R](
      contextIri: Seq[Iri],
      contextResolution: ResolverContextResolution,
      override val uuidF: UUIDF
  )(implicit api: JsonLdApi, rejectionMapper: Mapper[InvalidJsonLdRejection, R])
      extends JsonLdSourceProcessor {

    private val underlying = new JsonLdSourceParser[R](contextIri, uuidF)

    /**
      * Converts the passed ''source'' to JsonLD compacted and expanded. The @id value is extracted from the payload.
      * When no @id is present, one is generated using the base on the project suffixed with a randomly generated UUID.
      *
      * @param project
      *   the project with the base used to generate @id when needed and the @context when not provided on the source
      * @param source
      *   the Json payload
      * @return
      *   a tuple with the resulting @id iri, the compacted Json-LD and the expanded Json-LD
      */
    def apply(project: Project, source: Json)(implicit
        caller: Caller
    ): IO[R, (Iri, CompactedJsonLd, ExpandedJsonLd)] = {
      implicit val rcr: RemoteContextResolution = contextResolution(project.ref)
      underlying(project, source)
    }

    /**
      * Converts the passed ''source'' to JsonLD compacted and expanded. The @id value is extracted from the payload if
      * exists and compared to the passed ''iri''. If they aren't equal an [[UnexpectedId]] rejection is issued.
      *
      * @param project
      *   the project used to generate the @context when no @context is provided on the source
      * @param source
      *   the Json payload
      * @return
      *   a tuple with the compacted Json-LD and the expanded Json-LD
      */
    def apply(
        project: Project,
        iri: Iri,
        source: Json
    )(implicit caller: Caller): IO[R, (CompactedJsonLd, ExpandedJsonLd)] = {
      implicit val rcr: RemoteContextResolution = contextResolution(project.ref)
      underlying(project, iri, source)
    }

  }

  object JsonLdSourceResolvingParser {
    def apply[R](contextResolution: ResolverContextResolution, uuidF: UUIDF)(implicit
        api: JsonLdApi,
        rejectionMapper: Mapper[InvalidJsonLdRejection, R]
    ): JsonLdSourceResolvingParser[R] =
      new JsonLdSourceResolvingParser(Seq.empty, contextResolution, uuidF)
  }

  /**
    * Allows to parse the given json source and decode it into an ''A'' using static contexts
    */
  final class JsonLdSourceDecoder[R, A: JsonLdDecoder](contextIri: Iri, override val uuidF: UUIDF)(implicit
      api: JsonLdApi,
      rejectionMapper: Mapper[JsonLdRejection, R]
  ) extends JsonLdSourceProcessor {

    /**
      * Expands the passed ''source'' and attempt to decode it into an ''A'' The @id value is extracted from the
      * payload. When no @id is present, one is generated using the base on the project suffixed with a randomly
      * generated UUID.
      *
      * @param project
      *   the project with the base used to generate @id when needed and the @context when not provided on the source
      * @param source
      *   the Json payload
      * @return
      *   a tuple with the resulting @id iri and the decoded value
      */
    def apply(project: Project, source: Json)(implicit rcr: RemoteContextResolution): IO[R, (Iri, A)] = {
      for {
        (_, expanded) <- expandSource(project, source.addContext(contextIri))
        iri           <- getOrGenerateId(expanded.rootId.asIri, project)
        decodedValue  <- IO.fromEither(expanded.to[A]).mapError(DecodingFailed)
      } yield (iri, decodedValue)
    }.mapError(rejectionMapper.to)

    /**
      * Expands the passed ''source'' and attempt to decode it into an ''A'' The @id value is extracted from the payload
      * if exists and compared to the passed ''iri''. If they aren't equal an [[UnexpectedId]] rejection is issued.
      *
      * @param project
      *   the project with the base used to generate @id when needed and the @context when not provided on the source
      * @param source
      *   the Json payload
      * @return
      *   a tuple with the resulting @id iri and the decoded value
      */
    def apply(project: Project, iri: Iri, source: Json)(implicit
        rcr: RemoteContextResolution
    ): IO[R, A] = {
      for {
        (_, originalExpanded) <- expandSource(project, source.addContext(contextIri))
        expanded              <- checkAndSetSameId(iri, originalExpanded)
        decodedValue          <- IO.fromEither(expanded.to[A]).mapError(DecodingFailed)
      } yield decodedValue
    }.mapError(rejectionMapper.to)

  }

  /**
    * Allows to parse the given json source and decode it into an ''A'' using static and resolver-based contexts
    */
  final class JsonLdSourceResolvingDecoder[R, A: JsonLdDecoder](
      contextIri: Iri,
      contextResolution: ResolverContextResolution,
      override val uuidF: UUIDF
  )(implicit api: JsonLdApi, rejectionMapper: Mapper[JsonLdRejection, R])
      extends JsonLdSourceProcessor {

    private val underlying = new JsonLdSourceDecoder[R, A](contextIri, uuidF)

    /**
      * Expands the passed ''source'' and attempt to decode it into an ''A'' The @id value is extracted from the
      * payload. When no @id is present, one is generated using the base on the project suffixed with a randomly
      * generated UUID.
      *
      * @param project
      *   the project with the base used to generate @id when needed and the @context when not provided on the source
      * @param source
      *   the Json payload
      * @return
      *   a tuple with the resulting @id iri and the decoded value
      */
    def apply(project: Project, source: Json)(implicit caller: Caller): IO[R, (Iri, A)] = {
      implicit val rcr: RemoteContextResolution = contextResolution(project.ref)
      underlying(project, source)
    }

    /**
      * Expands the passed ''source'' and attempt to decode it into an ''A'' The @id value is extracted from the payload
      * if exists and compared to the passed ''iri''. If they aren't equal an [[UnexpectedId]] rejection is issued.
      *
      * @param project
      *   the project with the base used to generate @id when needed and the @context when not provided on the source
      * @param source
      *   the Json payload
      * @return
      *   a tuple with the resulting @id iri and the decoded value
      */
    def apply(project: Project, iri: Iri, source: Json)(implicit caller: Caller): IO[R, A] = {
      implicit val rcr: RemoteContextResolution = contextResolution(project.ref)
      underlying(project, iri, source)
    }
  }

}
