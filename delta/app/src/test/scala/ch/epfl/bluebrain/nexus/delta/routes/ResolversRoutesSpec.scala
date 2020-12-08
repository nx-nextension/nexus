package ch.epfl.bluebrain.nexus.delta.routes

import java.util.UUID
import akka.http.scaladsl.model.MediaRanges.`*/*`
import akka.http.scaladsl.model.MediaTypes.`text/event-stream`
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.{`Last-Event-ID`, Accept, OAuth2BearerToken}
import akka.http.scaladsl.server.Route
import ch.epfl.bluebrain.nexus.delta.rdf.Vocabulary.{contexts, nxv, schema}
import ch.epfl.bluebrain.nexus.delta.sdk.syntax._
import ch.epfl.bluebrain.nexus.delta.sdk.Permissions
import ch.epfl.bluebrain.nexus.delta.sdk.generators.ProjectGen
import ch.epfl.bluebrain.nexus.delta.sdk.model.Label
import ch.epfl.bluebrain.nexus.delta.sdk.model.acls.AclAddress
import ch.epfl.bluebrain.nexus.delta.sdk.model.identities.Identity.{Anonymous, Authenticated, Group, Subject}
import ch.epfl.bluebrain.nexus.delta.sdk.model.identities.{Caller, Identity}
import ch.epfl.bluebrain.nexus.delta.sdk.model.projects.{ApiMappings, ProjectRef}
import ch.epfl.bluebrain.nexus.delta.sdk.model.resolvers.ResolverType.{CrossProject, InProject}
import ch.epfl.bluebrain.nexus.delta.sdk.testkit.{AclSetup, IdentitiesDummy, ProjectSetup, ResolversDummy}
import ch.epfl.bluebrain.nexus.delta.sdk.utils.UUIDF
import ch.epfl.bluebrain.nexus.delta.utils.{RouteFixtures, RouteHelpers}
import ch.epfl.bluebrain.nexus.testkit._
import io.circe.Json
import io.circe.syntax._
import org.scalatest.matchers.should.Matchers
import org.scalatest.{Inspectors, OptionValues}

class ResolversRoutesSpec
    extends RouteHelpers
    with Matchers
    with CirceLiteral
    with CirceEq
    with IOFixedClock
    with IOValues
    with OptionValues
    with TestMatchers
    with Inspectors
    with RouteFixtures {

  private val uuid                  = UUID.randomUUID()
  implicit private val uuidF: UUIDF = UUIDF.fixed(uuid)

  private val asAlice = addCredentials(OAuth2BearerToken(alice.subject))
  private val asBob   = addCredentials(OAuth2BearerToken(bob.subject))

  private val org      = Label.unsafe("org")
  private val am       = ApiMappings(Map("nxv" -> nxv.base, "Person" -> schema.Person))
  private val projBase = nxv.base
  private val project  =
    ProjectGen.project("org", "project", uuid = uuid, orgUuid = uuid, base = projBase, mappings = am)
  private val project2 =
    ProjectGen.project("org", "project2", uuid = uuid, orgUuid = uuid, base = projBase, mappings = am)

  private val (_, projects) = {
    implicit val subject: Subject = Identity.Anonymous
    ProjectSetup
      .init(
        orgsToCreate = List(org),
        projectsToCreate = List(project, project2)
      )
      .accepted
  }

  private val identities = IdentitiesDummy(
    Caller(alice, Set(alice, Anonymous, Authenticated(realm), Group("group", realm))),
    Caller(bob, Set(bob))
  )

  private val acls = AclSetup
    .init(
      (Anonymous, AclAddress.Root, Set(Permissions.events.read)),
      (alice, AclAddress.Organization(org), Set(Permissions.resolvers.read, Permissions.resolvers.write)),
      (bob, AclAddress.Project(project.ref), Set(Permissions.resolvers.read, Permissions.resolvers.write))
    )
    .accepted

  private val resolvers = ResolversDummy(projects).accepted

  private val routes = Route.seal(ResolversRoutes(identities, acls, projects, resolvers))

  private def withId(id: String, payload: Json) =
    payload.deepMerge(Json.obj("@id" -> id.asJson))

  private val authorizationFailedResponse       = jsonContentOf("errors/authorization-failed.json")

  val inProjectPayload                    = jsonContentOf("resolvers/in-project-success.json")
  val crossProjectUseCurrentPayload       = jsonContentOf("resolvers/cross-project-use-current-caller-success.json")
  val crossProjectProvidedEntitiesPayload = jsonContentOf("resolvers/cross-project-provided-entities-success.json")

  "The Resolvers route" when {

    val newPriority = Json.obj("priority" -> 999.asJson)
    val tagPayload  = json"""{"tag": "my-tag", "rev": 1}"""

    "creating a resolver" should {

      def create(id: String, projectRef: ProjectRef, payload: Json) =
        List(
          iri"${nxv + id}-post" -> Post(s"/v1/resolvers/$projectRef", withId(s"${nxv + id}-post", payload).toEntity),
          iri"${nxv + id}-put"  -> Put(s"/v1/resolvers/$projectRef/$id-put", payload.toEntity),
          iri"${nxv + id}-put2" -> Put(
            s"/v1/resolvers/$projectRef/$id-put2",
            withId(s"${nxv + id}-put2", payload).toEntity
          )
        )

      "succeed for a in-project resolver" in {
        forAll(
          create("in-project", project.ref, inProjectPayload)
        ) { case (id, request) =>
          request ~> asBob ~> routes ~> check {
            status shouldEqual StatusCodes.Created
            response.asJson shouldEqual resolverMetadata(
              id,
              InProject,
              project.ref,
              createdBy = bob,
              updatedBy = bob,
              am = am
            )
          }

        }
      }

      "succeed for a cross-project resolver" in {
        forAll(
          create("cross-project-use-current", project2.ref, crossProjectUseCurrentPayload)
            ++ create("cross-project-provided-entities", project2.ref, crossProjectProvidedEntitiesPayload)
        ) { case (id, request) =>
          request ~> asAlice ~> routes ~> check {
            status shouldEqual StatusCodes.Created
            response.asJson shouldEqual resolverMetadata(
              id,
              CrossProject,
              project2.ref,
              createdBy = alice,
              updatedBy = alice,
              am = am
            )
          }

        }
      }

      "fail if it already exists" in {
        forAll(
          create("in-project", project.ref, inProjectPayload)
        ) { case (id, request) =>
          request ~> asAlice ~> routes ~> check {
            status shouldEqual StatusCodes.Conflict
            response.asJson shouldEqual jsonContentOf(
              "/resolvers/errors/already-exists.json",
              "id"      -> id,
              "projRef" -> project.ref
            )
          }
        }
      }

      "fail with a 400 if decoding fails" in {
        forAll(
          create("resolver-failed", project.ref, jsonContentOf("/resolvers/no-resolver-type-error.json"))
            ++ create("resolver-failed", project.ref, jsonContentOf("/resolvers/two-resolver-types-error.json"))
            ++ create("resolver-failed", project.ref, jsonContentOf("/resolvers/unknown-resolver-error.json"))
            ++ create(
              "resolver-failed",
              project.ref,
              jsonContentOf("/resolvers/cross-project-no-resolution-error.json")
            )
            ++ create(
              "resolver-failed",
              project.ref,
              jsonContentOf("/resolvers/cross-project-both-resolution-error.json")
            )
        ) { case (_, request) =>
          request ~> asAlice ~> routes ~> check {
            status shouldEqual StatusCodes.BadRequest
          }
        }
      }

      "fail if it there are no resolver/write permissions" in {
        forAll(
          create(genString(), project2.ref, inProjectPayload) ++ create(genString(), project2.ref, inProjectPayload)
        ) { case (_, request) =>
          request ~> asBob ~> routes ~> check {
            status shouldEqual StatusCodes.Forbidden
            response.asJson shouldEqual authorizationFailedResponse
          }

          request ~> routes ~> check {
            status shouldEqual StatusCodes.Forbidden
            response.asJson shouldEqual authorizationFailedResponse
          }
        }
      }

    }

    "updating a resolver" should {

      "succeed for a in-project resolver" in {
        Put(
          s"/v1/resolvers/${project.ref}/in-project-put?rev=1",
          inProjectPayload.deepMerge(newPriority).toEntity
        ) ~> asBob ~> routes ~> check {
          status shouldEqual StatusCodes.OK
          response.asJson shouldEqual resolverMetadata(
            nxv + "in-project-put",
            InProject,
            project.ref,
            rev = 2L,
            createdBy = bob,
            updatedBy = bob,
            am = am
          )
        }
      }

      "succeed for a cross-project resolver" in {
        Put(
          s"/v1/resolvers/${project2.ref}/cross-project-use-current-put?rev=1",
          crossProjectUseCurrentPayload.deepMerge(newPriority).toEntity
        ) ~> asAlice ~> routes ~> check {
          status shouldEqual StatusCodes.OK
          response.asJson shouldEqual resolverMetadata(
            nxv + "cross-project-use-current-put",
            CrossProject,
            project2.ref,
            rev = 2L,
            createdBy = alice,
            updatedBy = alice,
            am = am
          )
        }

        Put(
          s"/v1/resolvers/${project2.ref}/cross-project-provided-entities-put?rev=1",
          crossProjectProvidedEntitiesPayload.deepMerge(newPriority).toEntity
        ) ~> asAlice ~> routes ~> check {
          status shouldEqual StatusCodes.OK
          response.asJson shouldEqual resolverMetadata(
            nxv + "cross-project-provided-entities-put",
            CrossProject,
            project2.ref,
            rev = 2L,
            createdBy = alice,
            updatedBy = alice,
            am = am
          )
        }
      }

      "fail if the resolver doesn't exist" in {
        Put(
          s"/v1/resolvers/${project.ref}/in-project-put?rev=5",
          inProjectPayload.deepMerge(newPriority).toEntity
        ) ~> asBob ~> routes ~> check {
          status shouldEqual StatusCodes.Conflict
          response.asJson shouldEqual jsonContentOf(
            "/resolvers/errors/incorrect-rev.json",
            "provided" -> 5L,
            "expected" -> 2L
          )
        }
      }

      "fail if the revision is incorrect" in {
        Put(s"/v1/resolvers/${project.ref}/xxxx?rev=1", inProjectPayload.toEntity) ~> asAlice ~> routes ~> check {
          status shouldEqual StatusCodes.NotFound
          response.asJson shouldEqual jsonContentOf(
            "/resolvers/errors/not-found.json",
            "id"         -> (nxv + "xxxx"),
            "projectRef" -> project.ref
          )
        }
      }

      "fail if it there are no resolver/write permissions" in {
        forAll(
          List(
            Put(
              s"/v1/resolvers/${project.ref}/in-project-put?rev=1",
              inProjectPayload.deepMerge(newPriority).toEntity
            ) ~> routes,
            Put(
              s"/v1/resolvers/${project2.ref}/cross-project-use-current-put?rev=1",
              crossProjectUseCurrentPayload.deepMerge(newPriority).toEntity
            ) ~> asBob ~> routes
          )
        ) { request =>
          request ~> check {
            status shouldEqual StatusCodes.Forbidden
            response.asJson shouldEqual authorizationFailedResponse
          }
        }
      }
    }

    "tagging a resolver" should {

      "succeed" in {
        Post(
          s"/v1/resolvers/${project.ref}/in-project-put/tags?rev=2",
          tagPayload.toEntity
        ) ~> asAlice ~> routes ~> check {
          status shouldEqual StatusCodes.OK
          response.asJson shouldEqual resolverMetadata(
            nxv + "in-project-put",
            InProject,
            project.ref,
            rev = 3L,
            createdBy = bob,
            updatedBy = alice,
            am = am
          )
        }
      }

      "fail if it there are no resolver/write permissions" in {
        Post(
          s"/v1/resolvers/${project2.ref}/in-project-put/tags?rev=2",
          tagPayload.toEntity
        ) ~> asBob ~> routes ~> check {
          status shouldEqual StatusCodes.Forbidden
          response.asJson shouldEqual authorizationFailedResponse
        }
      }
    }

    "deprecating a resolver" should {

      "succeed" in {
        Delete(s"/v1/resolvers/${project.ref}/in-project-put?rev=3") ~> asAlice ~> routes ~> check {
          status shouldEqual StatusCodes.OK
          response.asJson shouldEqual
            resolverMetadata(
              nxv + "in-project-put",
              InProject,
              project.ref,
              rev = 4L,
              deprecated = true,
              createdBy = bob,
              updatedBy = alice,
              am = am
            )
        }
      }

      "fail if resolver has already been deprecated" in {
        Delete(s"/v1/resolvers/${project.ref}/in-project-put?rev=4") ~> asAlice ~> routes ~> check {
          status shouldEqual StatusCodes.BadRequest
          response.asJson shouldEqual
            jsonContentOf("/resolvers/errors/resolver-deprecated.json", "id" -> (nxv + "in-project-put"))
        }
      }

      "fail if no revision is provided" in {
        Delete(s"/v1/resolvers/${project.ref}/in-project-put") ~> asAlice ~> routes ~> check {
          status shouldEqual StatusCodes.BadRequest
          response.asJson shouldEqual
            jsonContentOf("/errors/missing-query-param.json", "field" -> "rev")
        }
      }

      "prevent further updates" in {
        Put(
          s"/v1/resolvers/${project.ref}/in-project-put?rev=4",
          inProjectPayload.toEntity
        ) ~> asBob ~> routes ~> check {
          status shouldEqual StatusCodes.BadRequest
          response.asJson shouldEqual jsonContentOf(
            "/resolvers/errors/resolver-deprecated.json",
            "id" -> (nxv + "in-project-put")
          )
        }
      }

      "prevent adding new tags" in {
        Post(
          s"/v1/resolvers/${project.ref}/in-project-put/tags?rev=4",
          tagPayload.toEntity
        ) ~> asAlice ~> routes ~> check {
          status shouldEqual StatusCodes.BadRequest
          response.asJson shouldEqual jsonContentOf(
            "/resolvers/errors/resolver-deprecated.json",
            "id" -> (nxv + "in-project-put")
          )
        }
      }

      "fail if it there are no resolver/write permissions" in {
        forAll(
          List(
            Delete(s"/v1/resolvers/${project.ref}/in-project-put?rev=1") ~> routes,
            Delete(s"/v1/resolvers/${project2.ref}/cross-project-use-current-put?rev=1") ~> asBob ~> routes
          )
        ) { request =>
          request ~> check {
            status shouldEqual StatusCodes.Forbidden
            response.asJson shouldEqual authorizationFailedResponse
          }
        }
      }
    }

    val inProjectLast = resolverMetadata(
      nxv + "in-project-put",
      InProject,
      project.ref,
      rev = 4L,
      deprecated = true,
      createdBy = bob,
      updatedBy = alice,
      am = am
    )
      .deepMerge(newPriority)
      .removeKeys("@context")

    val crossProjectUseCurrentLast = crossProjectUseCurrentPayload
      .deepMerge(newPriority)
      .deepMerge(
        resolverMetadata(
          nxv + "cross-project-use-current-put",
          CrossProject,
          project2.ref,
          rev = 2L,
          createdBy = alice,
          updatedBy = alice,
          am = am
        )
      )
      .removeKeys("@context")

    val crossProjectProvidedIdentitiesLast = crossProjectProvidedEntitiesPayload
      .deepMerge(newPriority)
      .deepMerge(
        resolverMetadata(
          nxv + "cross-project-provided-entities-put",
          CrossProject,
          project2.ref,
          rev = 2L,
          createdBy = alice,
          updatedBy = alice,
          am = am
        )
      )
      .removeKeys("@context")

    "fetching a resolver" should {

      val resolverContext     = json""" {"@context": "${contexts.resolvers}" } """
      val resolverMetaContext = json""" {"@context": ["${contexts.resolvers}", "${contexts.metadata}"]} """

      "get the latest version of an in-project resolver" in {
        Get(s"/v1/resolvers/${project.ref}/in-project-put") ~> asBob ~> routes ~> check {
          status shouldEqual StatusCodes.OK
          response.asJson shouldEqual inProjectLast.deepMerge(resolverMetaContext)
        }
      }

      "get the latest version of an cross-project resolver using current caller" in {
        Get(s"/v1/resolvers/${project2.ref}/cross-project-use-current-put") ~> asAlice ~> routes ~> check {
          status shouldEqual StatusCodes.OK
          response.asJson shouldEqual crossProjectUseCurrentLast.deepMerge(resolverMetaContext)
        }
      }

      "get the latest version of an cross-project resolver using provided entities" in {
        val ctx = json""" {"@context": [{"nxv" : "${nxv.base}"}, "${contexts.resolvers}", "${contexts.metadata}"]}"""
        Get(s"/v1/resolvers/${project2.ref}/cross-project-provided-entities-put") ~> asAlice ~> routes ~> check {
          status shouldEqual StatusCodes.OK
          response.asJson shouldEqual crossProjectProvidedIdentitiesLast.deepMerge(ctx)
        }
      }

      "get the version by revision" in {
        Get(s"/v1/resolvers/${project.ref}/in-project-put?rev=1") ~> asBob ~> routes ~> check {
          status shouldEqual StatusCodes.OK
          val expected = inProjectPayload
            .deepMerge(
              resolverMetadata(
                nxv + "in-project-put",
                InProject,
                project.ref,
                createdBy = bob,
                updatedBy = bob,
                am = am
              )
            )
            .deepMerge(resolverMetaContext)
          response.asJson shouldEqual expected
        }
      }

      "get the version by tag" in {
        Get(s"/v1/resolvers/${project.ref}/in-project-put?tag=my-tag") ~> asBob ~> routes ~> check {
          status shouldEqual StatusCodes.OK
          val expected = inProjectPayload
            .deepMerge(
              resolverMetadata(
                nxv + "in-project-put",
                InProject,
                project.ref,
                createdBy = bob,
                updatedBy = bob,
                am = am
              )
            )
            .deepMerge(resolverMetaContext)
          response.asJson shouldEqual expected
        }
      }

      "get the original payload" in {
        Get(s"/v1/resolvers/${project.ref}/in-project-put/source") ~> asBob ~> routes ~> check {
          status shouldEqual StatusCodes.OK
          val expected = inProjectPayload
            .deepMerge(newPriority)
            .deepMerge(resolverContext)
          response.asJson shouldEqual expected
        }
      }

      "get the original payload by revision" in {
        Get(s"/v1/resolvers/${project.ref}/in-project-put/source?rev=1") ~> asBob ~> routes ~> check {
          status shouldEqual StatusCodes.OK
          val expected = inProjectPayload
            .deepMerge(resolverContext)
          response.asJson shouldEqual expected
        }
      }

      "get the original payload by tag" in {
        Get(s"/v1/resolvers/${project.ref}/in-project-put/source?tag=my-tag") ~> asBob ~> routes ~> check {
          status shouldEqual StatusCodes.OK
          val expected = inProjectPayload
            .deepMerge(resolverContext)
          response.asJson shouldEqual expected
        }
      }

      "get the resolver tags" in {
        Get(s"/v1/resolvers/${project.ref}/in-project-put/tags") ~> asBob ~> routes ~> check {
          status shouldEqual StatusCodes.OK
          response.asJson shouldEqual json"""{"tags": [{"rev": 1, "tag": "my-tag"}]}""".addContext(contexts.tags)
        }
      }

      "fail if the resolver does not exist" in {
        Get(s"/v1/resolvers/${project.ref}/xxxx") ~> asBob ~> routes ~> check {
          status shouldEqual StatusCodes.NotFound
          response.asJson shouldEqual jsonContentOf(
            "/resolvers/errors/not-found.json",
            "id"         -> (nxv + "xxxx"),
            "projectRef" -> project.ref
          )
        }
      }

      "fail if the revision is not found" in {
        Get(s"/v1/resolvers/${project.ref}/in-project-put?rev=10") ~> asBob ~> routes ~> check {
          status shouldEqual StatusCodes.NotFound
          response.asJson shouldEqual jsonContentOf(
            "/errors/revision-not-found.json",
            "provided" -> 10L,
            "current"  -> 4L
          )
        }
      }

      "fail if it there are no resolver/read permissions" in {
        forAll(
          List(
            Get(s"/v1/resolvers/${project.ref}/in-project-put") ~> routes,
            Get(s"/v1/resolvers/${project2.ref}/cross-project-use-current-put") ~> asBob ~> routes
          )
        ) { request =>
          request ~> check {
            status shouldEqual StatusCodes.Forbidden
            response.asJson shouldEqual authorizationFailedResponse
          }
        }
      }
    }

    "listing the resolvers" should {

      def expectedResults(results: Json*): Json = {
        val ctx = json"""{"@context": ["${contexts.metadata}", "${contexts.search}", "${contexts.resolvers}"]}"""
        Json.obj("_total" -> Json.fromInt(results.size), "_results" -> Json.arr(results: _*)) deepMerge ctx
      }

      "return the deprecated resolvers the user has access to" in {
        Get(s"/v1/resolvers/${project.ref}?deprecated=true") ~> asBob ~> routes ~> check {
          status shouldEqual StatusCodes.OK
          response.asJson shouldEqual expectedResults(inProjectLast)
        }
      }

      "return the resolvers with revision 2" in {
        Get(s"/v1/resolvers/${project2.ref}?rev=2") ~> asAlice ~> routes ~> check {
          status shouldEqual StatusCodes.OK
          response.asJson should equalIgnoreArrayOrder(
            expectedResults(
              crossProjectUseCurrentLast,
              crossProjectProvidedIdentitiesLast.replace(Json.arr("nxv:Schema".asJson), Json.arr(nxv.Schema.asJson))
            )
          )
        }
      }

      "fail to list resolvers if the user has not access resolvers/read on the project" in {
        forAll(
          List(
            Get(s"/v1/resolvers/${project.ref}?deprecated=true") ~> routes,
            Get(s"/v1/resolvers/${project2.ref}") ~> asBob ~> routes
          )
        ) { request =>
          request ~> check {
            status shouldEqual StatusCodes.Forbidden
          }
        }
      }
    }

    "getting the events" should {

      "succeed from the given offset" in {
        Get("/v1/resolvers/events") ~> Accept(`*/*`) ~> `Last-Event-ID`("2") ~> routes ~> check {
          mediaType shouldBe `text/event-stream`
          response.asString.strip shouldEqual contentOf("resolvers/eventstream-2-14.txt").strip
        }
      }

      "fail to get event stream without permission" in {
        Get("/v1/resolvers/events") ~> Accept(`*/*`) ~> `Last-Event-ID`("1") ~> asBob ~> routes ~> check {
          response.asJson shouldEqual jsonContentOf("errors/authorization-failed.json")
          response.status shouldEqual StatusCodes.Forbidden
        }
      }
    }
  }
}