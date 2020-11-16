package ch.epfl.bluebrain.nexus.delta.routes

import akka.http.scaladsl.model.{StatusCodes, Uri}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive1, Route}
import cats.implicits._
import ch.epfl.bluebrain.nexus.delta.rdf.jsonld.context.{ContextValue, RemoteContextResolution}
import ch.epfl.bluebrain.nexus.delta.rdf.utils.JsonKeyOrdering
import ch.epfl.bluebrain.nexus.delta.routes.RealmsRoutes.RealmInput
import ch.epfl.bluebrain.nexus.delta.routes.RealmsRoutes.RealmInput._
import ch.epfl.bluebrain.nexus.delta.routes.marshalling.CirceUnmarshalling
import ch.epfl.bluebrain.nexus.delta.sdk.directives.AuthDirectives
import ch.epfl.bluebrain.nexus.delta.sdk.model.acls.AclAddress
import ch.epfl.bluebrain.nexus.delta.sdk.model.realms.RealmRejection._
import ch.epfl.bluebrain.nexus.delta.sdk.model.realms.{Realm, RealmRejection}
import ch.epfl.bluebrain.nexus.delta.sdk.model.search.PaginationConfig
import ch.epfl.bluebrain.nexus.delta.sdk.model.search.SearchParams.RealmSearchParams
import ch.epfl.bluebrain.nexus.delta.sdk.model.search.SearchResults._
import ch.epfl.bluebrain.nexus.delta.sdk.model.{BaseUri, Name}
import ch.epfl.bluebrain.nexus.delta.sdk.Permissions.{events, realms => realmsPermissions}
import ch.epfl.bluebrain.nexus.delta.sdk.{Acls, Identities, RealmResource, Realms}
import io.circe.Decoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredDecoder
import kamon.instrumentation.akka.http.TracingDirectives.operationName
import monix.execution.Scheduler

import scala.annotation.nowarn

class RealmsRoutes(identities: Identities, realms: Realms, acls: Acls)(implicit
    baseUri: BaseUri,
    paginationConfig: PaginationConfig,
    s: Scheduler,
    cr: RemoteContextResolution,
    ordering: JsonKeyOrdering
) extends AuthDirectives(identities, acls)
    with DeltaDirectives
    with CirceUnmarshalling {

  import baseUri.prefixSegment
  implicit val realmContext: ContextValue = Realm.context

  private def realmsSearchParams: Directive1[RealmSearchParams] =
    searchParams.tmap { case (deprecated, rev, createdBy, updatedBy) =>
      RealmSearchParams(None, deprecated, rev, createdBy, updatedBy)
    }

  def routes: Route =
    baseUriPrefix(baseUri.prefix) {
      extractCaller { implicit caller =>
        pathPrefix("realms") {
          concat(
            // List realms
            (get & extractUri & paginated & realmsSearchParams & pathEndOrSingleSlash) { (uri, pagination, params) =>
              operationName(s"$prefixSegment/realms") {
                authorizeFor(AclAddress.Root, realmsPermissions.read).apply {
                  implicit val searchEncoder: SearchEncoder[RealmResource] = searchResultsEncoder(pagination, uri)
                  completeSearch(realms.list(pagination, params))
                }
              }
            },
            // SSE realms
            (pathPrefix("events") & pathEndOrSingleSlash) {
              get {
                operationName(s"$prefixSegment/realms/events") {
                  authorizeFor(AclAddress.Root, events.read).apply {
                    lastEventId { offset =>
                      completeStream(realms.events(offset))
                    }
                  }
                }
              }
            },
            (label & pathEndOrSingleSlash) { id =>
              operationName(s"$prefixSegment/realms/{label}") {
                concat(
                  put {
                    authorizeFor(AclAddress.Root, realmsPermissions.write).apply {
                      parameter("rev".as[Long].?) {
                        case Some(rev) =>
                          // Update realm
                          entity(as[RealmInput]) { case RealmInput(name, openIdConfig, logo) =>
                            completeIO(realms.update(id, rev, name, openIdConfig, logo).map(_.void))
                          }
                        case None      =>
                          // Create realm
                          entity(as[RealmInput]) { case RealmInput(name, openIdConfig, logo) =>
                            completeIO(StatusCodes.Created, realms.create(id, name, openIdConfig, logo).map(_.void))
                          }
                      }
                    }
                  },
                  get {
                    authorizeFor(AclAddress.Root, realmsPermissions.read).apply {
                      parameter("rev".as[Long].?) {
                        case Some(rev) => // Fetch realm at specific revision
                          completeIOOpt(realms.fetchAt(id, rev).leftWiden[RealmRejection])
                        case None      => // Fetch realm
                          completeUIOOpt(realms.fetch(id))
                      }
                    }
                  },
                  // Deprecate realm
                  delete {
                    authorizeFor(AclAddress.Root, realmsPermissions.write).apply {
                      parameter("rev".as[Long]) { rev => completeIO(realms.deprecate(id, rev).map(_.void)) }
                    }
                  }
                )
              }
            }
          )
        }
      }
    }
}

object RealmsRoutes {
  import ch.epfl.bluebrain.nexus.delta.sdk.instances._

  @nowarn("cat=unused")
  implicit final private val configuration: Configuration = Configuration.default.withStrictDecoding

  final private[routes] case class RealmInput(name: Name, openIdConfig: Uri, logo: Option[Uri])
  private[routes] object RealmInput {
    implicit val realmDecoder: Decoder[RealmInput] = deriveConfiguredDecoder[RealmInput]
  }

  /**
    * @return the [[Route]] for realms
    */
  def apply(identities: Identities, realms: Realms, acls: Acls)(implicit
      baseUri: BaseUri,
      paginationConfig: PaginationConfig,
      s: Scheduler,
      cr: RemoteContextResolution,
      ordering: JsonKeyOrdering
  ): Route =
    new RealmsRoutes(identities, realms, acls).routes

}