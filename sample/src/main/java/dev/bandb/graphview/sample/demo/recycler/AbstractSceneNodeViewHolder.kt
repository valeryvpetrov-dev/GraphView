package dev.bandb.graphview.sample.demo.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.bandb.graphview.sample.demo.graph.AbstractSceneNode

abstract class AbstractSceneNodeViewHolder<T : AbstractSceneNode>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(node: T)
}