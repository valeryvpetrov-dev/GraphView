package dev.bandb.graphview.sample.demo.recycler

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import androidx.recyclerview.widget.RecyclerView
import dev.bandb.graphview.graph.Node
import dev.bandb.graphview.layouts.tree.BuchheimWalkerConfiguration
import dev.bandb.graphview.layouts.tree.BuchheimWalkerLayoutManager
import dev.bandb.graphview.layouts.tree.TreeEdgeDecoration
import dev.bandb.graphview.sample.demo.graph.SceneNode

class OptionEdgeDecoration : TreeEdgeDecoration() {

    companion object {
        private const val TEXT_MARGIN = 2
    }

    private val textPaint: TextPaint = TextPaint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
        textSize = 20f
    }
    private val textBackgroundPaint: TextPaint = TextPaint(textPaint).apply {
        color = Color.YELLOW
    }
    private val textFontMetrics = Paint.FontMetrics().also {
        textPaint.getFontMetrics(it)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val layout = parent.layoutManager as BuchheimWalkerLayoutManager
        val adapter = parent.adapter as ScriptGraphAdapter

        val configuration = layout.configuration
        val graph = adapter.graph ?: return
        when (configuration.orientation) {
            BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM -> {
                graph.nodes
                    .filterIsInstance<SceneNode>()
                    .forEach { node ->
                        graph.successorsOf(node).forEachIndexed { i, child ->
                            drawText(c, node.options[i].id, node, child, configuration)
                        }
                    }
            }
            else -> TODO("Not yet implemented")
        }
    }

    private fun drawText(
        canvas: Canvas,
        text: String,
        parent: Node,
        child: Node,
        configuration: BuchheimWalkerConfiguration
    ) {
        val textWidth = textPaint.measureText(text)
        val x = child.x + child.width / 2f - textWidth / 2f
        val y = child.y - (configuration.levelSeparation / 2f) + textFontMetrics.bottom
        canvas.drawRect(
            x - TEXT_MARGIN,
            y + textFontMetrics.top - TEXT_MARGIN,
            x + textWidth + TEXT_MARGIN,
            y + textFontMetrics.bottom + TEXT_MARGIN,
            textBackgroundPaint
        )
        canvas.drawText(text, x, y, textPaint)
    }
}