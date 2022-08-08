package dev.bandb.graphview.sample.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.bandb.graphview.sample.graph.AbstractSceneNode

abstract class AbstractSceneNodeViewHolder<T : AbstractSceneNode>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(node: T)
}