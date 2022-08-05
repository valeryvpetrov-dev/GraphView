package dev.bandb.graphview.sample.demo.recycler.decoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.text.TextPaint
import androidx.recyclerview.widget.RecyclerView
import dev.bandb.graphview.graph.Node
import dev.bandb.graphview.layouts.tree.BuchheimWalkerConfiguration
import dev.bandb.graphview.layouts.tree.BuchheimWalkerLayoutManager
import dev.bandb.graphview.layouts.tree.TreeEdgeDecoration
import dev.bandb.graphview.sample.demo.graph.OptionEdge
import dev.bandb.graphview.sample.demo.graph.SceneNode
import dev.bandb.graphview.sample.demo.recycler.ScriptGraphAdapter

class OptionEdgeDecoration : TreeEdgeDecoration() {

    companion object {
        private const val TEXT_MARGIN = 2
        private const val ARROW_WIDTH = 20
        private const val ARROW_HALF_WIDTH = ARROW_WIDTH / 2f
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

    private val arrowPaint = Paint().apply {
        color = linePaint.color
        strokeWidth = linePaint.strokeWidth
        style = Paint.Style.FILL_AND_STROKE
        strokeJoin = Paint.Join.MITER
    }
    private val arrowPath = Path()

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
                        graph.successorsOf(node).forEach { child ->
                            val optionEdge = graph.getEdgeBetween(node, child) as OptionEdge
                            drawText(c, optionEdge.option.id, node, child, configuration)
                            drawArrow(c, child)
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

    private fun drawArrow(canvas: Canvas, child: Node) {
        val x = child.x + child.width / 2f
        val y = child.y
        with(arrowPath) {
            reset()
            moveTo(x, y)
            lineTo(x - ARROW_HALF_WIDTH, y - ARROW_WIDTH) // Top left
            lineTo(x + ARROW_HALF_WIDTH, y - ARROW_WIDTH) // Top right
            lineTo(x, y) // Back
            close()
        }
        canvas.drawPath(arrowPath, arrowPaint)
    }
}