package dev.bandb.graphview.sample.demo.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import dev.bandb.graphview.AbstractGraphAdapter
import dev.bandb.graphview.sample.R
import dev.bandb.graphview.sample.demo.graph.SceneNode
import dev.bandb.graphview.sample.demo.graph.UnknownSceneNode

class ScriptGraphAdapter(
    val onUnknownSceneClicked: (UnknownSceneNode) -> Unit,
    val onSceneClicked: (SceneNode) -> Unit
) : AbstractGraphAdapter<AbstractSceneNodeViewHolder<*>>() {

    companion object {
        private const val VIEW_TYPE_UNKNOWN_SCENE = 0
        private const val VIEW_TYPE_SCENE = 1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractSceneNodeViewHolder<*> {
        return when (viewType) {
            VIEW_TYPE_UNKNOWN_SCENE -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_unknown_scene_node, parent, false
                )
                UnknownSceneNodeViewHolder(view, onUnknownSceneClicked)
            }
            VIEW_TYPE_SCENE -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_scene_node, parent, false
                )
                SceneNodeViewHolder(view, onSceneClicked)
            }
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: AbstractSceneNodeViewHolder<*>, position: Int) {
        return when (val node = getNode(position)) {
            is UnknownSceneNode -> {
                val unknownSceneNodeViewHolder = holder as UnknownSceneNodeViewHolder
                unknownSceneNodeViewHolder.onBind(node)
            }
            is SceneNode -> {
                val sceneNodeViewHolder = holder as SceneNodeViewHolder
                sceneNodeViewHolder.onBind(node)
            }
            else -> throw IllegalArgumentException("Unknown view type $node")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val node = getNode(position)) {
            is UnknownSceneNode -> VIEW_TYPE_UNKNOWN_SCENE
            is SceneNode -> VIEW_TYPE_SCENE
            else -> throw IllegalArgumentException("Unknown node type $node")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun replace(old: UnknownSceneNode, new: SceneNode) {
        val graph = graph ?: return
        val parents = graph.getInEdges(old).map { it.source }
        with(graph) {
            // Replace old with new
            parents.forEach { parent -> addEdge(parent, new) }
            removeNode(old)
            val oldPosition = graph.getNodePosition(old)
            notifyItemRemoved(oldPosition)
            notifyItemInserted(oldPosition)
            // Inflate options
            new.options.forEach { option ->
                val unknownSceneNodeForOption = UnknownSceneNode.getInstance()
                addEdge(new, unknownSceneNodeForOption)
                notifyItemInserted(getNodePosition(unknownSceneNodeForOption))
            }
        }
    }
}