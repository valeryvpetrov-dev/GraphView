package dev.bandb.graphview.sample.recycler

import android.view.View
import android.widget.TextView
import dev.bandb.graphview.sample.R
import dev.bandb.graphview.sample.graph.SceneNode

class SceneNodeViewHolder(itemView: View, val onClicked: (SceneNode) -> Unit) :
    AbstractSceneNodeViewHolder<SceneNode>(itemView) {

    override fun onBind(node: SceneNode) {
        itemView.setOnClickListener { onClicked(node) }
        with(itemView.findViewById<TextView>(R.id.textView)) {
            text = node.id
        }
    }
}