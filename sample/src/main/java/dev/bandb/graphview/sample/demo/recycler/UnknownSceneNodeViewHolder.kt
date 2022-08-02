package dev.bandb.graphview.sample.demo.recycler

import android.view.View
import dev.bandb.graphview.sample.demo.graph.UnknownSceneNode

class UnknownSceneNodeViewHolder(itemView: View, val onClicked: (UnknownSceneNode) -> Unit) :
    AbstractSceneNodeViewHolder<UnknownSceneNode>(itemView) {

    override fun onBind(node: UnknownSceneNode) {
        itemView.setOnClickListener { onClicked(node) }
    }
}