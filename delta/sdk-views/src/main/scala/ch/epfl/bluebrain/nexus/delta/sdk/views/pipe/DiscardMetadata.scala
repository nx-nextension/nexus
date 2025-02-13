package ch.epfl.bluebrain.nexus.delta.sdk.views.pipe

import ch.epfl.bluebrain.nexus.delta.rdf.graph.Graph
import ch.epfl.bluebrain.nexus.delta.sdk.views.model.ViewData.IndexingData
import ch.epfl.bluebrain.nexus.delta.sdk.views.pipe.Pipe.withoutConfig
import ch.epfl.bluebrain.nexus.delta.sdk.views.pipe.PipeDef.noConfig
import monix.bio.Task

/**
  * Excludes metadata from being indexed
  */
object DiscardMetadata {

  val name: String = "discardMetadata"

  val pipe: Pipe =
    withoutConfig(
      name,
      (data: IndexingData) => Task.some(data.copy(metadataGraph = Graph.empty(data.id)))
    )

  def apply(): PipeDef = noConfig(name)

}
