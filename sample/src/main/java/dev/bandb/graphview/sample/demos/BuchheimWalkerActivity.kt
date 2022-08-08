package dev.bandb.graphview.sample.demos

import androidx.recyclerview.widget.RecyclerView
import dev.bandb.graphview.graph.Graph
import dev.bandb.graphview.layouts.tree.BuchheimWalkerConfiguration
import dev.bandb.graphview.layouts.tree.BuchheimWalkerLayoutManager
import dev.bandb.graphview.sample.graph.UnknownSceneNode
import dev.bandb.graphview.sample.recycler.decoration.OptionEdgeDecoration

class BuchheimWalkerActivity : DemoActivity() {

    override fun createGraph(): Graph {
        return Graph().apply {
            addNode(UnknownSceneNode())
        }
    }

    override fun createLayoutManager(): RecyclerView.LayoutManager {
        val configuration = BuchheimWalkerConfiguration.Builder()
            .setSiblingSeparation(100)
            .setLevelSeparation(100)
            .setSubtreeSeparation(100)
            .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
            .build()
        return BuchheimWalkerLayoutManager(this, configuration)
    }

    override fun createItemDecoration(): RecyclerView.ItemDecoration {
        return OptionEdgeDecoration()
    }
}
