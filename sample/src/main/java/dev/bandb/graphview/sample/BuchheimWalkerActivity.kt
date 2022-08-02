package dev.bandb.graphview.sample

import android.view.Menu
import android.view.MenuItem
import dev.bandb.graphview.graph.Graph
import dev.bandb.graphview.layouts.tree.BuchheimWalkerConfiguration
import dev.bandb.graphview.layouts.tree.BuchheimWalkerLayoutManager
import dev.bandb.graphview.layouts.tree.TreeEdgeDecoration
import dev.bandb.graphview.sample.GraphActivity
import dev.bandb.graphview.sample.R

class BuchheimWalkerActivity : GraphActivity() {

    public override fun setLayoutManager() {
        val configuration = BuchheimWalkerConfiguration.Builder()
                .setSiblingSeparation(100)
                .setLevelSeparation(100)
                .setSubtreeSeparation(100)
                .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
                .build()
        recyclerView.layoutManager = BuchheimWalkerLayoutManager(this, configuration)
    }

    public override fun setEdgeDecoration() {
        recyclerView.addItemDecoration(TreeEdgeDecoration())
    }

    public override fun createGraph(): Graph {
        return Graph()
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
}