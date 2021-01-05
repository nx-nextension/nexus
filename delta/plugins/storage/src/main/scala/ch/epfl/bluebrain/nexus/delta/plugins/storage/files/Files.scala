package ch.epfl.bluebrain.nexus.delta.plugins.storage.files

import akka.actor.typed.ActorSystem
import akka.actor.{ActorSystem => ClassicActorSystem}
import akka.http.scaladsl.model.ContentTypes.`application/octet-stream`
import akka.http.scaladsl.model.{ContentType, HttpEntity, Uri}
import akka.persistence.query.Offset
import cats.effect.Clock
import cats.syntax.all._
import ch.epfl.bluebrain.nexus.delta.kernel.RetryStrategy
import ch.epfl.bluebrain.nexus.delta.plugins.storage.files.Files.{moduleType, next, FilesAggregate}
import ch.epfl.bluebrain.nexus.delta.plugins.storage.files.model.Digest.{ComputedDigest, NotComputedDigest}
import ch.epfl.bluebrain.nexus.delta.plugins.storage.files.model.FileCommand._
import ch.epfl.bluebrain.nexus.delta.plugins.storage.files.model.FileEvent._
import ch.epfl.bluebrain.nexus.delta.plugins.storage.files.model.FileRejection._
import ch.epfl.bluebrain.nexus.delta.plugins.storage.files.model.FileState.{Current, Initial}
import ch.epfl.bluebrain.nexus.delta.plugins.storage.files.model._
import ch.epfl.bluebrain.nexus.delta.plugins.storage.storages.model.StorageRejection.{InvalidStorageId, StorageIsDeprecated}
import ch.epfl.bluebrain.nexus.delta.plugins.storage.storages.model.{DigestAlgorithm, Storage}
import ch.epfl.bluebrain.nexus.delta.plugins.storage.storages.Storages
import ch.epfl.bluebrain.nexus.delta.plugins.storage.utils.EventLogUtils
import ch.epfl.bluebrain.nexus.delta.rdf.IriOrBNode.Iri
import ch.epfl.bluebrain.nexus.delta.sdk.jsonld.JsonLdSourceParser
import ch.epfl.bluebrain.nexus.delta.sdk.jsonld.JsonLdSourceParser.generateId
import ch.epfl.bluebrain.nexus.delta.sdk.model.acls.AclAddress
import ch.epfl.bluebrain.nexus.delta.sdk.model.identities.Caller
import ch.epfl.bluebrain.nexus.delta.sdk.model.identities.Identity.Subject
import ch.epfl.bluebrain.nexus.delta.sdk.model.permissions.Permission
import ch.epfl.bluebrain.nexus.delta.sdk.model.projects.{Project, ProjectRef}
import ch.epfl.bluebrain.nexus.delta.sdk.model._
import ch.epfl.bluebrain.nexus.delta.sdk.syntax._
import ch.epfl.bluebrain.nexus.delta.sdk.utils.{IOUtils, UUIDF}
import ch.epfl.bluebrain.nexus.delta.sdk._
import ch.epfl.bluebrain.nexus.sourcing._
import ch.epfl.bluebrain.nexus.sourcing.processor.EventSourceProcessor.persistenceId
import ch.epfl.bluebrain.nexus.sourcing.processor.ShardedAggregate
import fs2.Stream
import monix.bio.{IO, Task, UIO}
import monix.execution.Scheduler

import java.util.UUID

/**
  * Operations for handling files
  */
final class Files(
    formDataExtractor: FormDataExtractor,
    aggregate: FilesAggregate,
    eventLog: EventLog[Envelope[FileEvent]],
    acls: Acls,
    orgs: Organizations,
    projects: Projects,
    storages: Storages
)(implicit uuidF: UUIDF, system: ClassicActorSystem, sc: Scheduler) {

  // format: off
  private val testStorageRef = ResourceRef.Revision(iri"http://localhost/test", 1)
  private val testAttributes = FileAttributes(UUID.randomUUID(), "http://localhost", Uri.Path.Empty, "", `application/octet-stream`, 0, ComputedDigest(DigestAlgorithm.default, "value"))
  // format: on

  /**
    * Create a new file where the id is  self generated
    *
    * @param storageId  the optional storage identifier to expand as the id of the storage. When None, the default storage is used
    * @param projectRef the project where the file will belong
    * @param entity     the http FormData entity
    */
  def create(
      storageId: Option[IdSegment],
      projectRef: ProjectRef,
      entity: HttpEntity
  )(implicit caller: Caller): IO[FileRejection, FileResource] = {
    for {
      project               <- projects.fetchActiveProject(projectRef)
      iri                   <- generateId(project)
      _                     <- test(CreateFile(iri, projectRef, testStorageRef, testAttributes, caller.subject))
      (storageRef, storage) <- fetchActiveStorage(storageId, project)
      attributes            <- extractFileAttributes(iri, entity, storage)
      res                   <- eval(CreateFile(iri, projectRef, storageRef, attributes, caller.subject), project)
    } yield res
  }.named("createFile", moduleType)

  /**
    * Create a new file with the provided id
    *
    * @param id         the file identifier to expand as the iri of the file
    * @param storageId  the optional storage identifier to expand as the id of the storage. When None, the default storage is used
    * @param projectRef the project where the file will belong
    * @param entity     the http FormData entity
    */
  def create(
      id: IdSegment,
      storageId: Option[IdSegment],
      projectRef: ProjectRef,
      entity: HttpEntity
  )(implicit caller: Caller): IO[FileRejection, FileResource] = {
    for {
      project               <- projects.fetchActiveProject(projectRef)
      iri                   <- expandIri(id, project)
      _                     <- test(CreateFile(iri, projectRef, testStorageRef, testAttributes, caller.subject))
      (storageRef, storage) <- fetchActiveStorage(storageId, project)
      attributes            <- extractFileAttributes(iri, entity, storage)
      res                   <- eval(CreateFile(iri, projectRef, storageRef, attributes, caller.subject), project)
    } yield res
  }.named("createFile", moduleType)

  /**
    * Update an existing file
    *
    * @param id         the file identifier to expand as the iri of the file
    * @param storageId  the optional storage identifier to expand as the id of the storage. When None, the default storage is used
    * @param projectRef the project where the file will belong
    * @param rev        the current revision of the file
    * @param entity     the http FormData entity
    */
  def update(
      id: IdSegment,
      storageId: Option[IdSegment],
      projectRef: ProjectRef,
      rev: Long,
      entity: HttpEntity
  )(implicit caller: Caller): IO[FileRejection, FileResource] = {
    for {
      project               <- projects.fetchActiveProject(projectRef)
      iri                   <- expandIri(id, project)
      _                     <- test(UpdateFile(iri, projectRef, testStorageRef, testAttributes, rev, caller.subject))
      (storageRef, storage) <- fetchActiveStorage(storageId, project)
      attributes            <- extractFileAttributes(iri, entity, storage)
      res                   <- eval(UpdateFile(iri, projectRef, storageRef, attributes, rev, caller.subject), project)
    } yield res
  }.named("updateFile", moduleType)

  /**
    * Update an existing file attributes
    *
    * @param id         the file identifier to expand as the iri of the file
    * @param projectRef the project where the file will belong
    * @param mediaType  the media type of the file
    * @param bytes      the size of the file file in bytes
    * @param digest     the digest information of the file
    * @param rev        the current revision of the file
    */
  def updateAttributes(
      id: IdSegment,
      projectRef: ProjectRef,
      mediaType: ContentType,
      bytes: Long,
      digest: Digest,
      rev: Long
  )(implicit caller: Caller): IO[FileRejection, FileResource] = {
    for {
      project <- projects.fetchActiveProject(projectRef)
      iri     <- expandIri(id, project)
      subject  = caller.subject
      _       <- test(UpdateFileAttributes(iri, projectRef, mediaType, bytes, digest, rev, subject))
      res     <- eval(UpdateFileAttributes(iri, projectRef, mediaType, bytes, digest, rev, subject), project)
    } yield res
  }.named("updateFileAttributes", moduleType)

  /**
    * Add a tag to an existing file
    *
    * @param id         the file identifier to expand as the iri of the storage
    * @param projectRef the project where the file belongs
    * @param tag        the tag name
    * @param tagRev     the tag revision
    * @param rev        the current revision of the file
    */
  def tag(
      id: IdSegment,
      projectRef: ProjectRef,
      tag: TagLabel,
      tagRev: Long,
      rev: Long
  )(implicit subject: Subject): IO[FileRejection, FileResource] = {
    for {
      project <- projects.fetchActiveProject(projectRef)
      iri     <- expandIri(id, project)
      res     <- eval(TagFile(iri, projectRef, tagRev, tag, rev, subject), project)
    } yield res
  }.named("tagFile", moduleType)

  /**
    * Deprecate an existing file
    *
    * @param id         the file identifier to expand as the iri of the file
    * @param projectRef the project where the file belongs
    * @param rev        the current revision of the file
    */
  def deprecate(
      id: IdSegment,
      projectRef: ProjectRef,
      rev: Long
  )(implicit subject: Subject): IO[FileRejection, FileResource] = {
    for {
      project <- projects.fetchActiveProject(projectRef)
      iri     <- expandIri(id, project)
      res     <- eval(DeprecateFile(iri, projectRef, rev, subject), project)
    } yield res
  }.named("deprecateFile", moduleType)

  /**
    * Fetch the last version of a file content
    *
    * @param id        the file identifier to expand as the iri of the file
    * @param project   the project where the storage belongs
    */
  def fetchContent(
      id: IdSegment,
      project: ProjectRef
  )(implicit caller: Caller): IO[FileRejection, AkkaSource] =
    fetchContent(id, project, None).named("fetchFileContent", moduleType)

  /**
    * Fetches the file content at a given revision
    *
    * @param id        the file identifier to expand as the iri of the file
    * @param project   the project where the file belongs
    * @param rev       the current revision of the file
    */
  def fetchContentAt(
      id: IdSegment,
      project: ProjectRef,
      rev: Long
  )(implicit caller: Caller): IO[FileRejection, AkkaSource] =
    fetchContent(id, project, Some(rev)).named("fetchFileContentAt", moduleType)

  /**
    * Fetches a file content by tag.
    *
    * @param id        the file identifier to expand as the iri of the file
    * @param project   the project where the file belongs
    * @param tag       the tag revision
    */
  def fetchContentBy(
      id: IdSegment,
      project: ProjectRef,
      tag: TagLabel
  )(implicit caller: Caller): IO[FileRejection, AkkaSource] =
    fetch(id, project, None)
      .flatMap { resource =>
        resource.value.tags.get(tag) match {
          case Some(rev) => fetchContentAt(id, project, rev).leftMap(_ => TagNotFound(tag))
          case None      => IO.raiseError(TagNotFound(tag))
        }
      }
      .named("fetchFileContentByTag", moduleType)

  /**
    * Fetch the last version of a file
    *
    * @param id         the file identifier to expand as the iri of the file
    * @param project the project where the storage belongs
    */
  def fetch(id: IdSegment, project: ProjectRef): IO[FileRejection, FileResource] =
    fetch(id, project, None).named("fetchFile", moduleType)

  /**
    * Fetches the file at a given revision
    *
    * @param id      the file identifier to expand as the iri of the file
    * @param project the project where the file belongs
    * @param rev     the current revision of the file
    */
  def fetchAt(
      id: IdSegment,
      project: ProjectRef,
      rev: Long
  ): IO[FileRejection, FileResource] =
    fetch(id, project, Some(rev)).named("fetchFileAt", moduleType)

  /**
    * Fetches a file by tag.
    *
    * @param id        the file identifier to expand as the iri of the file
    * @param project   the project where the file belongs
    * @param tag       the tag revision
    */
  def fetchBy(
      id: IdSegment,
      project: ProjectRef,
      tag: TagLabel
  ): IO[FileRejection, FileResource] =
    fetch(id, project, None)
      .flatMap { resource =>
        resource.value.tags.get(tag) match {
          case Some(rev) => fetchAt(id, project, rev).leftMap(_ => TagNotFound(tag))
          case None      => IO.raiseError(TagNotFound(tag))
        }
      }
      .named("fetchFileByTag", moduleType)

  /**
    * A non terminating stream of events for files. After emitting all known events it sleeps until new events
    * are recorded.
    *
    * @param projectRef the project reference where the files belongs
    * @param offset     the last seen event offset; it will not be emitted by the stream
    */
  def events(
      projectRef: ProjectRef,
      offset: Offset
  ): IO[FileRejection, Stream[Task, Envelope[FileEvent]]] =
    projects
      .fetchProject(projectRef)
      .as(eventLog.eventsByTag(s"${Projects.moduleType}=$projectRef", offset))

  /**
    * A non terminating stream of events for storages. After emitting all known events it sleeps until new events
    * are recorded.
    *
    * @param organization the organization label reference where the file belongs
    * @param offset       the last seen event offset; it will not be emitted by the stream
    */
  def events(
      organization: Label,
      offset: Offset
  ): IO[WrappedOrganizationRejection, Stream[Task, Envelope[FileEvent]]] =
    orgs
      .fetchOrganization(organization)
      .as(eventLog.eventsByTag(s"${Organizations.moduleType}=$organization", offset))

  /**
    * A non terminating stream of events for files. After emitting all known events it sleeps until new events
    * are recorded.
    *
    * @param offset the last seen event offset; it will not be emitted by the stream
    */
  def events(offset: Offset): Stream[Task, Envelope[FileEvent]] =
    eventLog.eventsByTag(moduleType, offset)

  private def fetch(
      id: IdSegment,
      projectRef: ProjectRef,
      rev: Option[Long]
  ): IO[FileRejection, FileResource] =
    for {
      project <- projects.fetchProject(projectRef)
      iri     <- expandIri(id, project)
      state   <- rev.fold(currentState(projectRef, iri))(stateAt(projectRef, iri, _))
      res     <- IO.fromOption(state.toResource(project.apiMappings, project.base), FileNotFound(iri, projectRef))
    } yield res

  private def fetchContent(
      id: IdSegment,
      projectRef: ProjectRef,
      rev: Option[Long]
  )(implicit caller: Caller): IO[FileRejection, AkkaSource] =
    for {
      file    <- fetch(id, projectRef, rev)
      storage <- storages.fetch(file.value.storage, projectRef)
      _       <- authorizeFor(projectRef, storage.value.storageValue.readPermission)
      content <- storage.value.fetchFile(file.value.attributes).leftMap(FetchRejection(file.id, storage.id, _))
    } yield content

  private def eval(cmd: FileCommand, project: Project): IO[FileRejection, FileResource] =
    for {
      evaluationResult <- aggregate.evaluate(identifier(cmd.project, cmd.id), cmd).mapError(_.value)
      resourceOpt       = evaluationResult.state.toResource(project.apiMappings, project.base)
      res              <- IO.fromOption(resourceOpt, UnexpectedInitialState(cmd.id, project.ref))
    } yield res

  private def test(cmd: FileCommand) =
    aggregate.dryRun(identifier(cmd.project, cmd.id), cmd).mapError(_.value)

  private def currentState(project: ProjectRef, iri: Iri): IO[FileRejection, FileState] =
    aggregate.state(identifier(project, iri))

  private def stateAt(project: ProjectRef, iri: Iri, rev: Long) =
    EventLogUtils
      .fetchStateAt(eventLog, persistenceId(moduleType, identifier(project, iri)), rev, Initial, next)
      .leftMap(RevisionNotFound(rev, _))

  private def identifier(project: ProjectRef, id: Iri): String =
    s"${project}_$id"

  private def fetchActiveStorage(
      storageIdOpt: Option[IdSegment],
      project: Project
  )(implicit caller: Caller): IO[FileRejection, (ResourceRef.Revision, Storage)] =
    storageIdOpt match {
      case Some(storageId) =>
        for {
          iri     <- expandStorageIri(storageId, project)
          storage <- storages.fetch(ResourceRef(iri), project.ref)
          _       <- if (storage.deprecated) IO.raiseError(WrappedStorageRejection(StorageIsDeprecated(iri))) else IO.unit
          _       <- authorizeFor(project.ref, storage.value.storageValue.writePermission)
        } yield ResourceRef.Revision(storage.id, storage.rev) -> storage.value
      case None            =>
        storages
          .fetchDefault(project.ref)
          .map(storage => ResourceRef.Revision(storage.id, storage.rev) -> storage.value)
          .leftMap(WrappedStorageRejection)
    }

  private def extractFileAttributes(iri: Iri, entity: HttpEntity, storage: Storage): IO[FileRejection, FileAttributes] =
    for {
      (description, source) <- formDataExtractor(iri, entity, storage.storageValue.maxFileSize)
      attributes            <- storage.saveFile(description, source).leftMap(SaveRejection(iri, storage.id, _))
    } yield attributes

  private def expandStorageIri(segment: IdSegment, project: Project): IO[WrappedStorageRejection, Iri] =
    JsonLdSourceParser.expandIri(segment, project, id => WrappedStorageRejection(InvalidStorageId(id)))

  private def expandIri(segment: IdSegment, project: Project): IO[InvalidFileId, Iri] =
    JsonLdSourceParser.expandIri(segment, project, InvalidFileId.apply)

  private def authorizeFor(
      projectRef: ProjectRef,
      permission: Permission
  )(implicit caller: Caller): IO[AuthorizationFailed, Unit] = {
    val path = AclAddress.Project(projectRef)
    acls.fetchWithAncestors(path).map(_.exists(caller.identities, permission, path)).flatMap {
      case false => IO.raiseError(AuthorizationFailed)
      case true  => IO.unit
    }
  }

}

@SuppressWarnings(Array("MaxParameters"))
object Files {

  /**
    * The files module type.
    */
  final val moduleType: String = "file"

  private[files] type FilesAggregate =
    Aggregate[String, FileState, FileCommand, FileEvent, FileRejection]

  /**
    * Constructs a Files instance
    *
    * @param eventLog the event log for FileEvent
    * @param acls     the acls operations bundle
    * @param orgs     the organizations operations bundle
    * @param projects the projects operations bundle
    * @param storages the storages operations bundle
    */
  final def apply(
      config: AggregateConfig,
      eventLog: EventLog[Envelope[FileEvent]],
      acls: Acls,
      orgs: Organizations,
      projects: Projects,
      storages: Storages
  )(implicit
      uuidF: UUIDF,
      clock: Clock[UIO],
      scheduler: Scheduler,
      as: ActorSystem[Nothing]
  ): UIO[Files] = {
    implicit val classicAs: ClassicActorSystem = as.classicSystem
    aggregate(config).map(apply(_, eventLog, acls, orgs, projects, storages))
  }

  private def apply(
      agg: FilesAggregate,
      eventLog: EventLog[Envelope[FileEvent]],
      acls: Acls,
      orgs: Organizations,
      projects: Projects,
      storages: Storages
  )(implicit system: ClassicActorSystem, sc: Scheduler, uuidF: UUIDF) =
    new Files(FormDataExtractor.apply, agg, eventLog, acls, orgs, projects, storages)

  private def aggregate(config: AggregateConfig)(implicit
      as: ActorSystem[Nothing],
      clock: Clock[UIO]
  ) = {
    val definition = PersistentEventDefinition(
      entityType = moduleType,
      initialState = Initial,
      next = next,
      evaluate = evaluate,
      tagger = (event: FileEvent) =>
        Set(
          moduleType,
          s"${Projects.moduleType}=${event.project}",
          s"${Organizations.moduleType}=${event.project.organization}"
        ),
      snapshotStrategy = config.snapshotStrategy.combinedStrategy(
        SnapshotStrategy.SnapshotPredicate((state: FileState, _: FileEvent, _: Long) => state.deprecated)
      ),
      stopStrategy = config.stopStrategy.persistentStrategy
    )

    ShardedAggregate.persistentSharded(
      definition = definition,
      config = config.processor,
      retryStrategy = RetryStrategy.alwaysGiveUp
      // TODO: configure the number of shards
    )
  }

  private[files] def next(
      state: FileState,
      event: FileEvent
  ): FileState = {
    // format: off
    def created(e: FileCreated): FileState = state match {
      case Initial     => Current(e.id, e.project, e.storage, e.attributes, Map.empty, e.rev, deprecated = false,  e.instant, e.subject, e.instant, e.subject)
      case s: Current  => s
    }

    def updated(e: FileUpdated): FileState = state match {
      case Initial    => Initial
      case s: Current => s.copy(rev = e.rev, storage = e.storage, attributes = e.attributes, updatedAt = e.instant, updatedBy = e.subject)
    }

    def updatedAttributes(e: FileAttributesUpdated): FileState = state match {
      case Initial    => Initial
      case s: Current => s.copy(rev = e.rev, attributes = s.attributes.copy( mediaType = e.mediaType, bytes = e.bytes, digest = e.digest), updatedAt = e.instant, updatedBy = e.subject)
    }

    def tagAdded(e: FileTagAdded): FileState = state match {
      case Initial    => Initial
      case s: Current => s.copy(rev = e.rev, tags = s.tags + (e.tag -> e.targetRev), updatedAt = e.instant, updatedBy = e.subject)
    }
    // format: on

    def deprecated(e: FileDeprecated): FileState = state match {
      case Initial    => Initial
      case s: Current => s.copy(rev = e.rev, deprecated = true, updatedAt = e.instant, updatedBy = e.subject)
    }

    event match {
      case e: FileCreated           => created(e)
      case e: FileUpdated           => updated(e)
      case e: FileAttributesUpdated => updatedAttributes(e)
      case e: FileTagAdded          => tagAdded(e)
      case e: FileDeprecated        => deprecated(e)
    }
  }

  private[files] def evaluate(
      state: FileState,
      cmd: FileCommand
  )(implicit clock: Clock[UIO]): IO[FileRejection, FileEvent] = {

    def create(c: CreateFile) = state match {
      case Initial =>
        IOUtils.instant.map(FileCreated(c.id, c.project, c.storage, c.attributes, 1L, _, c.subject))
      case _       =>
        IO.raiseError(FileAlreadyExists(c.id, c.project))
    }

    def update(c: UpdateFile) = state match {
      case Initial                                                => IO.raiseError(FileNotFound(c.id, c.project))
      case s: Current if s.rev != c.rev                           => IO.raiseError(IncorrectRev(c.rev, s.rev))
      case s: Current if s.deprecated                             => IO.raiseError(FileIsDeprecated(c.id))
      case s: Current if s.attributes.digest == NotComputedDigest => IO.raiseError(DigestNotComputed(c.id))
      case s: Current                                             =>
        IOUtils.instant.map(FileUpdated(c.id, c.project, c.storage, c.attributes, s.rev + 1L, _, c.subject))
    }

    def updateAttributes(c: UpdateFileAttributes) = state match {
      case Initial                      => IO.raiseError(FileNotFound(c.id, c.project))
      case s: Current if s.rev != c.rev => IO.raiseError(IncorrectRev(c.rev, s.rev))
      case s: Current if s.deprecated   => IO.raiseError(FileIsDeprecated(c.id))
      case s: Current                   =>
        // format: off
        IOUtils.instant
          .map(FileAttributesUpdated(c.id, c.project, c.mediaType, c.bytes, c.digest, s.rev + 1L, _, c.subject))
      // format: on
    }

    def tag(c: TagFile) = state match {
      case Initial                                                => IO.raiseError(FileNotFound(c.id, c.project))
      case s: Current if s.rev != c.rev                           => IO.raiseError(IncorrectRev(c.rev, s.rev))
      case s: Current if s.deprecated                             => IO.raiseError(FileIsDeprecated(c.id))
      case s: Current if c.targetRev <= 0L || c.targetRev > s.rev => IO.raiseError(RevisionNotFound(c.targetRev, s.rev))
      case s: Current                                             =>
        IOUtils.instant.map(FileTagAdded(c.id, c.project, c.targetRev, c.tag, s.rev + 1L, _, c.subject))
    }

    def deprecate(c: DeprecateFile) = state match {
      case Initial                      => IO.raiseError(FileNotFound(c.id, c.project))
      case s: Current if s.rev != c.rev => IO.raiseError(IncorrectRev(c.rev, s.rev))
      case s: Current if s.deprecated   => IO.raiseError(FileIsDeprecated(c.id))
      case s: Current                   => IOUtils.instant.map(FileDeprecated(c.id, c.project, s.rev + 1L, _, c.subject))
    }

    cmd match {
      case c: CreateFile           => create(c)
      case c: UpdateFile           => update(c)
      case c: UpdateFileAttributes => updateAttributes(c)
      case c: TagFile              => tag(c)
      case c: DeprecateFile        => deprecate(c)
    }
  }

}