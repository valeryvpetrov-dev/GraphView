package dev.bandb.graphview.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import dev.bandb.graphview.AbstractGraphAdapter
import dev.bandb.graphview.graph.Graph
import dev.bandb.graphview.layouts.tree.BuchheimWalkerConfiguration
import dev.bandb.graphview.layouts.tree.BuchheimWalkerLayoutManager
import dev.bandb.graphview.layouts.tree.TreeEdgeDecoration
import dev.bandb.graphview.sample.demo.graph.UnknownSceneNode
import dev.bandb.graphview.sample.demo.recycler.ScriptGraphAdapter
import java.util.*

class BuchheimWalkerActivity : AppCompatActivity() {

    private lateinit var graph: Graph
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ScriptGraphAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buchheim_walker)

        graph = createGraph()
        recyclerView = findViewById(R.id.recycler)
        setLayoutManager()
        setEdgeDecoration()
        setupGraphView(graph)

        setupToolbar()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_buchheim_walker_orientations, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val builder = BuchheimWalkerConfiguration.Builder()
            .setSiblingSeparation(100)
            .setLevelSeparation(300)
            .setSubtreeSeparation(300)
        val itemId = item.itemId
        if (itemId == R.id.topToBottom) {
            builder.setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
        } else if (itemId == R.id.bottomToTop) {
            builder.setOrientation(BuchheimWalkerConfiguration.ORIENTATION_BOTTOM_TOP)
        } else if (itemId == R.id.leftToRight) {
            builder.setOrientation(BuchheimWalkerConfiguration.ORIENTATION_LEFT_RIGHT)
        } else if (itemId == R.id.rightToLeft) {
            builder.setOrientation(BuchheimWalkerConfiguration.ORIENTATION_RIGHT_LEFT)
        } else {
            return super.onOptionsItemSelected(item)
        }
        recyclerView.layoutManager = BuchheimWalkerLayoutManager(this, builder.build())
        recyclerView.adapter = adapter
        return true
    }

    private fun createGraph(): Graph {
        return Graph().apply {
            addNode(UnknownSceneNode())
        }
    }

    private fun setLayoutManager() {
        val configuration = BuchheimWalkerConfiguration.Builder()
                .setSiblingSeparation(100)
                .setLevelSeparation(100)
                .setSubtreeSeparation(100)
                .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
                .build()
        recyclerView.layoutManager = BuchheimWalkerLayoutManager(this, configuration)
    }

    private fun setEdgeDecoration() {
        recyclerView.addItemDecoration(TreeEdgeDecoration())
    }

    private fun setupGraphView(graph: Graph) {
        adapter = ScriptGraphAdapter(
            onUnknownSceneClicked = {
                Toast.makeText(this, "On unknown scene clicked: $it", Toast.LENGTH_SHORT)
                    .show()
            },
            onSceneClicked = {
                Toast.makeText(this, "On scene clicked: $it", Toast.LENGTH_SHORT)
                    .show()
            }
        ).apply {
            this.submitGraph(graph)
            recyclerView.adapter = this
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }
}