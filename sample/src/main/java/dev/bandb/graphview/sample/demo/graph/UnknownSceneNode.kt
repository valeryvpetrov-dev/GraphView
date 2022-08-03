package dev.bandb.graphview.sample.demo.graph

class UnknownSceneNode : AbstractSceneNode(ID) {

    companion object {
        const val ID = "Unknown"

        fun getInstance(): UnknownSceneNode = UnknownSceneNode()
    }

    override fun toString(): String {
        return "UnknownSceneNode() ${super.toString()}"
    }
}