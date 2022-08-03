package dev.bandb.graphview.sample.demo.graph

class SceneNode(id: String, val options: List<Option>) : AbstractSceneNode(id) {

    override fun toString(): String {
        return "SceneNode(options=$options) ${super.toString()}"
    }
}