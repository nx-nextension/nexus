package ch.epfl.bluebrain.nexus.delta.service.organizations

import ch.epfl.bluebrain.nexus.delta.kernel.CacheIndexingConfig
import ch.epfl.bluebrain.nexus.delta.sdk.cache.KeyValueStoreConfig
import ch.epfl.bluebrain.nexus.delta.sdk.model.search.PaginationConfig
import ch.epfl.bluebrain.nexus.delta.sourcing.config.AggregateConfig
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

/**
  * Configuration for the Organizations module.
  *
  * @param aggregate
  *   configuration of the underlying aggregate
  * @param keyValueStore
  *   configuration of the underlying key/value store
  * @param pagination
  *   configuration for how pagination should behave in listing operations
  * @param cacheIndexing
  *   configuration of the cache indexing process
  */
final case class OrganizationsConfig(
    aggregate: AggregateConfig,
    keyValueStore: KeyValueStoreConfig,
    pagination: PaginationConfig,
    cacheIndexing: CacheIndexingConfig
)

object OrganizationsConfig {
  implicit final val orgsConfigReader: ConfigReader[OrganizationsConfig] =
    deriveReader[OrganizationsConfig]
}
