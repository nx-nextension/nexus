package ch.epfl.bluebrain.nexus.delta.plugins

import ch.epfl.bluebrain.nexus.delta.plugins.storage.model.Storage
import ch.epfl.bluebrain.nexus.delta.sdk.Permissions.resources
import ch.epfl.bluebrain.nexus.delta.sdk.model.ResourceF
import ch.epfl.bluebrain.nexus.delta.sdk.model.permissions.Permission
import ch.epfl.bluebrain.nexus.delta.sdk.syntax._

package object storage {

  /**
    * Type alias for a storage specific resource.
    */
  type StorageResource = ResourceF[Storage]

  /**
    * Storage schemas
    */
  object schemas {
    val storage = iri"https://bluebrain.github.io/nexus/schemas/storage.json"
  }

  /**
    * Storage contexts
    */
  object contexts {
    val storage = iri"https://bluebrain.github.io/nexus/contexts/storage.json"
  }

  object permissions {
    final val read: Permission  = resources.read
    final val write: Permission = Permission.unsafe("storages/write")
  }
}