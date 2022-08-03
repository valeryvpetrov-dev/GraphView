package dev.bandb.graphview.sample.demo.graph

class UnknownSceneNode : AbstractSceneNode(ID) {

    companion object {
        const val ID = "Unknown"
    }

    override fun toString(): String {
        return "UnknownSceneNode() ${super.toString()}"
    }
}