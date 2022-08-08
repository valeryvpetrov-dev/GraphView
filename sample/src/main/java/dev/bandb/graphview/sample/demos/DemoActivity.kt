package dev.bandb.graphview.sample.demos

import android.annotation.SuppressLint
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
import com.otaliastudios.zoom.ZoomLayout
import dev.bandb.graphview.graph.Graph
import dev.bandb.graphview.sample.R
import dev.bandb.graphview.sample.graph.Option
import dev.bandb.graphview.sample.graph.SceneNode
import dev.bandb.graphview.sample.recycler.ScriptGraphAdapter

abstract class DemoActivity : AppCompatActivity() {

    protected lateinit var graph: Graph
    protected lateinit var recyclerView: RecyclerView
    protected lateinit var zoom: ZoomLayout
    protected lateinit var adapter: ScriptGraphAdapter

    protected lateinit var scenesAvailable: MutableList<SceneNode>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        recyclerView = findViewById<RecyclerView?>(R.id.recycler).apply {
            itemAnimator = null
        }
        zoom = findViewById(R.id.zoomLayout)
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
        inflater.inflate(R.menu.menu_demo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear -> {
                initializeGraph()
                initializeAvailableScenes()
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
        return true
    }

    abstract fun createGraph(): Graph

    abstract fun createLayoutManager(): RecyclerView.LayoutManager

    abstract fun createItemDecoration(): RecyclerView.ItemDecoration

    private fun setLayoutManager() {
        recyclerView.layoutManager = createLayoutManager()
    }

    private fun setEdgeDecoration() {
        recyclerView.addItemDecoration(createItemDecoration())
    }

    private fun setAdapter() {
        ScriptGraphAdapter(
            onUnknownSceneClicked = { srcNode ->
                showAvailableScenesChooser { selectedNode ->
                    adapter.replace(srcNode, selectedNode)
                    zoomOut()
                }
            },
            onSceneClicked = {
                Toast.makeText(this, "On scene clicked: $it", Toast.LENGTH_SHORT)
                    .show()
            }
        ).apply {
            adapter = this
            recyclerView.adapter = this
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initializeGraph() {
        val graph = createGraph()
        this.graph = graph
        adapter.submitGraph(graph)
        adapter.notifyDataSetChanged()
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

    private fun zoomOut() {
        zoom.post { zoom.zoomTo(zoom.getMinZoom(), true) }
    }
}
