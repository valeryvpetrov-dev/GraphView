package dev.bandb.graphview.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import dev.bandb.graphview.graph.Graph
import dev.bandb.graphview.layouts.tree.BuchheimWalkerConfiguration
import dev.bandb.graphview.layouts.tree.BuchheimWalkerLayoutManager
import dev.bandb.graphview.layouts.tree.TreeEdgeDecoration
import dev.bandb.graphview.sample.demo.graph.Option
import dev.bandb.graphview.sample.demo.graph.SceneNode
import dev.bandb.graphview.sample.demo.graph.UnknownSceneNode
import dev.bandb.graphview.sample.demo.recycler.ScriptGraphAdapter

class BuchheimWalkerActivity : AppCompatActivity() {

    private lateinit var graph: Graph
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ScriptGraphAdapter

    private lateinit var scenesAvailable: MutableList<SceneNode>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buchheim_walker)
        recyclerView = findViewById(R.id.recycler)
        setLayoutManager()
        setEdgeDecoration()
        setAdapter()

        initializeGraph()
        initializeAvailableScenes()

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
        when (item.itemId) {
            R.id.clear -> {
                initializeGraph()
                initializeAvailableScenes()
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
        recyclerView.layoutManager = BuchheimWalkerLayoutManager(this, builder.build())
        recyclerView.adapter = adapter
        return true
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

    private fun setAdapter() {
        ScriptGraphAdapter(
            onUnknownSceneClicked = { srcNode ->
                showAvailableScenesChooser { selectedNode ->
                    adapter.replace(srcNode, selectedNode)
                }
            },
            onSceneClicked = {
                Toast.makeText(this, "On scene clicked: $it", Toast.LENGTH_SHORT)
                    .show()
            }
        ).apply {
            adapter = this
            submitGraph(graph)
            recyclerView.adapter = this
        }
    }

    private fun initializeGraph() {
        val graph = Graph().apply {
            addNode(UnknownSceneNode())
        }
        this.graph = graph
        adapter.submitGraph(graph)
    }

    private fun initializeAvailableScenes() {
        scenesAvailable = mutableListOf(
            SceneNode("Scene 1", listOf(Option("Option 1.1"))),
            SceneNode("Scene 2", listOf(Option("Option 2.1"), Option("Option 2.2"))),
            SceneNode("Scene 3", listOf(Option("Option 3.1"))),
            SceneNode("Scene 4", listOf(Option("Option 4.1"))),
            SceneNode("Scene 5", listOf())
        )
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun showAvailableScenesChooser(onSceneChosen: (SceneNode) -> Unit) {
        val addSceneDialog = BottomSheetDialog(this).apply {
            setContentView(R.layout.bottom_sheet_add_scene)
        }
        val scenesAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            scenesAvailable.map { it.id }
        )
        addSceneDialog.findViewById<ListView>(R.id.scenesList)?.let {
            it.adapter = scenesAdapter
            it.onItemClickListener =
                OnItemClickListener { adapterView: AdapterView<*>?, view: View?, i: Int, l: Long ->
                    val selectedScene = scenesAvailable[l.toInt()]
                    scenesAvailable.removeAt(l.toInt())
                    addSceneDialog.dismiss()
                    onSceneChosen(selectedScene)
                }
        }
        addSceneDialog.show()
    }
}