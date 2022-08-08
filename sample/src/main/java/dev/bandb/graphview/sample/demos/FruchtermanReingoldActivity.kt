package dev.bandb.graphview.sample.demos

import androidx.recyclerview.widget.RecyclerView
import dev.bandb.graphview.decoration.edge.ArrowEdgeDecoration
import dev.bandb.graphview.graph.Graph
import dev.bandb.graphview.layouts.energy.FruchtermanReingoldLayoutManager
import dev.bandb.graphview.sample.graph.UnknownSceneNode

class FruchtermanReingoldActivity : DemoActivity() {

    override fun createGraph(): Graph {
        return Graph().apply {
            addNode(UnknownSceneNode())
        }
    }

    override fun createLayoutManager(): RecyclerView.LayoutManager {
        return FruchtermanReingoldLayoutManager(this, 1000)
    }

    override fun createItemDecoration(): RecyclerView.ItemDecoration {
        return ArrowEdgeDecoration()
    }
}
