package ch.epfl.bluebrain.nexus.delta.sdk.model.projects

import ch.epfl.bluebrain.nexus.delta.rdf.Vocabulary.{contexts, nxv}
import ch.epfl.bluebrain.nexus.delta.rdf.jsonld.context.ContextValue
import ch.epfl.bluebrain.nexus.delta.rdf.jsonld.context.JsonLdContext.keywords
import ch.epfl.bluebrain.nexus.delta.sdk.implicits._
import ch.epfl.bluebrain.nexus.delta.sdk.model.Event.ProjectScopedEvent
import ch.epfl.bluebrain.nexus.delta.sdk.model.identities.Identity
import ch.epfl.bluebrain.nexus.delta.sdk.model.identities.Identity.Subject
import ch.epfl.bluebrain.nexus.delta.sdk.model.projects.ProjectEvent.ProjectCreated
import ch.epfl.bluebrain.nexus.delta.sdk.model.{BaseUri, Label, ResourceUris}
import io.circe.Encoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredEncoder
import io.circe.syntax._

import java.time.Instant
import java.util.UUID
import scala.annotation.nowarn

/**
  * Enumeration of Project event types.
  */
sealed trait ProjectEvent extends ProjectScopedEvent {

  /**
    * @return
    *   the project ref
    */
  def project: ProjectRef = ProjectRef(organizationLabel, label)

  /**
    * @return
    *   the project label
    */
  def label: Label

  /**
    * @return
    *   the project uuid
    */
  def uuid: UUID

  /**
    * @return
    *   the parent organization label
    */
  def organizationLabel: Label

  /**
    * @return
    *   the parent organization unique identifier
    */
  def organizationUuid: UUID

  /**
    * @return
    *   true if the event is [[ProjectCreated]], false otherwise
    */
  def isCreated: Boolean = this match {
    case _: ProjectCreated => true
    case _                 => false
  }

}

object ProjectEvent {

  /**
    * Evidence that a project has been created.
    *
    * @param label
    *   the project label
    * @param uuid
    *   the project uuid
    * @param organizationLabel
    *   the parent organization label
    * @param organizationUuid
    *   the parent organization uuid
    * @param rev
    *   the project revision
    * @param description
    *   an optional project description
    * @param apiMappings
    *   the project API mappings
    * @param base
    *   the base Iri for generated resource IDs ending with ''/'' or ''#''
    * @param vocab
    *   an optional vocabulary for resources with no context ending with ''/'' or ''#''
    * @param instant
    *   the timestamp associated to this event
    * @param subject
    *   the identity associated to this event
    */
  final case class ProjectCreated(
      label: Label,
      uuid: UUID,
      override val organizationLabel: Label,
      organizationUuid: UUID,
      rev: Long,
      description: Option[String],
      apiMappings: ApiMappings,
      base: PrefixIri,
      vocab: PrefixIri,
      instant: Instant,
      subject: Subject
  ) extends ProjectEvent

  /**
    * Evidence that a project has been updated.
    *
    * @param label
    *   the project label
    * @param uuid
    *   the project uuid
    * @param organizationLabel
    *   the parent organization label
    * @param organizationUuid
    *   the parent organization uuid
    * @param description
    *   an optional project description
    * @param apiMappings
    *   the project API mappings
    * @param base
    *   the base Iri for generated resource IDs ending with ''/'' or ''#''
    * @param vocab
    *   an optional vocabulary for resources with no context ending with ''/'' or ''#''
    * @param rev
    *   the revision number that this event generates
    * @param instant
    *   the timestamp associated to this event
    * @param subject
    *   the identity associated to this event
    */
  final case class ProjectUpdated(
      label: Label,
      uuid: UUID,
      override val organizationLabel: Label,
      organizationUuid: UUID,
      rev: Long,
      description: Option[String],
      apiMappings: ApiMappings,
      base: PrefixIri,
      vocab: PrefixIri,
      instant: Instant,
      subject: Subject
  ) extends ProjectEvent

  /**
    * Evidence that a project has been deprecated.
    *
    * @param label
    *   the label (segment) of the project
    * @param uuid
    *   the permanent identifier for the project
    * @param organizationLabel
    *   the parent organization label
    * @param organizationUuid
    *   the parent organization uuid
    * @param rev
    *   the revision number that this event generates
    * @param instant
    *   the timestamp associated to this event
    * @param subject
    *   the identity associated to this event
    */
  final case class ProjectDeprecated(
      label: Label,
      uuid: UUID,
      override val organizationLabel: Label,
      organizationUuid: UUID,
      rev: Long,
      instant: Instant,
      subject: Subject
  ) extends ProjectEvent

  /**
    * Evidence that a project has been marked for deletion.
    *
    * @param label
    *   the label (segment) of the project
    * @param uuid
    *   the permanent identifier for the project
    * @param organizationLabel
    *   the parent organization label
    * @param organizationUuid
    *   the parent organization uuid
    * @param rev
    *   the revision number that this event generates
    * @param instant
    *   the timestamp associated to this event
    * @param subject
    *   the identity associated to this event
    */
  final case class ProjectMarkedForDeletion(
      label: Label,
      uuid: UUID,
      override val organizationLabel: Label,
      organizationUuid: UUID,
      rev: Long,
      instant: Instant,
      subject: Subject
  ) extends ProjectEvent

  private val context = ContextValue(contexts.metadata, contexts.projects)

  @nowarn("cat=unused")
  implicit private val config: Configuration = Configuration.default
    .withDiscriminator(keywords.tpe)
    .copy(transformMemberNames = {
      case "label"             => nxv.label.prefix
      case "uuid"              => nxv.uuid.prefix
      case "organizationLabel" => nxv.organizationLabel.prefix
      case "organizationUuid"  => nxv.organizationUuid.prefix
      case "rev"               => nxv.rev.prefix
      case "instant"           => nxv.instant.prefix
      case "subject"           => nxv.eventSubject.prefix
      case other               => other
    })

  @nowarn("cat=unused")
  implicit def projectEventEncoder(implicit baseUri: BaseUri): Encoder.AsObject[ProjectEvent] = {
    implicit val subjectEncoder: Encoder[Subject] = Identity.subjectIdEncoder
    Encoder.encodeJsonObject.contramapObject { event =>
      deriveConfiguredEncoder[ProjectEvent]
        .encodeObject(event)
        .add("_projectId", ResourceUris.project(event.project).accessUri.asJson)
        .add(nxv.resourceId.prefix, ResourceUris.project(event.project).accessUri.asJson)
        .add(keywords.context, context.value)
    }
  }
}
