package ch.epfl.bluebrain.nexus.delta.plugins.elasticsearch.indexing

import cats.syntax.functor._
import ch.epfl.bluebrain.nexus.delta.plugins.elasticsearch.client.{ElasticSearchBulk, IndexLabel}
import ch.epfl.bluebrain.nexus.delta.plugins.elasticsearch.model.IndexingData.graphPredicates
import ch.epfl.bluebrain.nexus.delta.plugins.elasticsearch.model.{contexts, IndexingData}
import ch.epfl.bluebrain.nexus.delta.rdf.IriOrBNode.Iri
import ch.epfl.bluebrain.nexus.delta.rdf.Triple.subject
import ch.epfl.bluebrain.nexus.delta.rdf.Vocabulary
import ch.epfl.bluebrain.nexus.delta.rdf.Vocabulary.nxv
import ch.epfl.bluebrain.nexus.delta.rdf.jsonld.context.JsonLdContext.keywords
import ch.epfl.bluebrain.nexus.delta.rdf.jsonld.context.{ContextValue, RemoteContextResolution}
import ch.epfl.bluebrain.nexus.delta.sdk.EventExchange.EventExchangeValue
import ch.epfl.bluebrain.nexus.delta.sdk.implicits._
import ch.epfl.bluebrain.nexus.delta.sdk.model.{BaseUri, ResourceF}
import io.circe.Json
import monix.bio.Task

final case class ElasticSearchIndexingStreamEntry(
    resource: ResourceF[IndexingData]
)(implicit cr: RemoteContextResolution, baseUri: BaseUri) {

  private val ctx: ContextValue =
    ContextValue(contexts.elasticsearchIndexing, Vocabulary.contexts.metadataAggregate)

  /**
    * Deletes or indexes the current resource into ElasticSearch as a Document depending on the passed filters
    */
  def deleteOrIndex(
      idx: IndexLabel,
      includeMetadata: Boolean,
      includeDeprecated: Boolean,
      sourceAsText: Boolean
  ): Task[ElasticSearchBulk] = {
    if (resource.deprecated && !includeDeprecated) delete(idx)
    else index(idx, includeMetadata, sourceAsText)
  }

  /**
    * Deletes the current resource Document
    */
  def delete(idx: IndexLabel): Task[ElasticSearchBulk] =
    Task.pure(ElasticSearchBulk.Delete(idx, resource.id.toString))

  /**
    * Generates an ElasticSearch Bulk Index query with the Document to be added to the index ''idx''
    */
  def index(
      idx: IndexLabel,
      includeMetadata: Boolean,
      sourceAsText: Boolean
  ): Task[ElasticSearchBulk] =
    index(idx, includeMetadata, sourceAsText, ctx)

  /**
    * Generates an ElasticSearch Bulk Index query with the Document to be added to the index ''idx''
    */
  def index(
      idx: IndexLabel,
      includeMetadata: Boolean,
      sourceAsText: Boolean,
      context: ContextValue
  ): Task[ElasticSearchBulk] =
    toDocument(includeMetadata, sourceAsText, context).map(doc =>
      ElasticSearchBulk.Index(idx, resource.id.toString, doc)
    )

  /**
    * Checks if the current resource contains some of the schemas passed as ''resourceSchemas''
    */
  def containsSchema(resourceSchemas: Set[Iri]): Boolean =
    resourceSchemas.isEmpty || resourceSchemas.contains(resource.schema.iri)

  /**
    * Checks if the current resource contains some of the types passed as ''resourceTypes''
    */
  def containsTypes[A](resourceTypes: Set[Iri]): Boolean =
    resourceTypes.isEmpty || resourceTypes.intersect(resource.types).nonEmpty

  private def toDocument(includeMetadata: Boolean, sourceAsText: Boolean, context: ContextValue): Task[Json] = {
    val predGraph = resource.value.selectPredicatesGraph
    val metaGraph = resource.value.metadataGraph
    Option
      .when(includeMetadata)(resource.void.toGraph.map(_ ++ predGraph ++ metaGraph))
      .getOrElse(Task.pure(predGraph))
      .flatMap {
        case graph if sourceAsText =>
          val jsonLd = graph.add(nxv.originalSource.iri, resource.value.source.noSpaces).toCompactedJsonLd(context)
          jsonLd.map(_.json.removeKeys(keywords.context))
        case graph                 =>
          val jsonLd = graph.toCompactedJsonLd(context)
          jsonLd.map(ld => mergeJsonLd(resource.value.source, ld.json)).map(_.removeAllKeys(keywords.context))
      }
  }

  private def mergeJsonLd(a: Json, b: Json): Json =
    if (a.isEmpty()) b
    else if (b.isEmpty()) a
    else a deepMerge b

}

object ElasticSearchIndexingStreamEntry {

  /**
    * Converts the resource retrieved from an event exchange to [[ElasticSearchIndexingStreamEntry]].
    * It generates an [[IndexingData]] out of the relevant parts of the resource for elasticsearch indexing
    */
  def fromEventExchange[A, M](
      exchangedValue: EventExchangeValue[A, M]
  )(implicit cr: RemoteContextResolution, baseUri: BaseUri): Task[ElasticSearchIndexingStreamEntry] = {
    val resource = exchangedValue.value.toResource
    val encoder  = exchangedValue.value.encoder
    val source   = exchangedValue.value.toSource
    val metadata = exchangedValue.metadata
    val id       = resource.resolvedId
    for {
      graph        <- encoder.graph(resource.value)
      rootGraph     = graph.replaceRootNode(id)
      metaGraph    <- metadata.encoder.graph(metadata.value)
      rootMetaGraph = metaGraph.replaceRootNode(id)
      s             = source.removeAllKeys(keywords.context)
      fGraph        = rootGraph.filter { case (s, p, _) => s == subject(id) && graphPredicates.contains(p) }
      data          = resource.as(IndexingData(fGraph, rootMetaGraph, s))
    } yield ElasticSearchIndexingStreamEntry(data)
  }
}