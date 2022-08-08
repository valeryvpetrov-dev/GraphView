package dev.bandb.graphview.sample.graph

import dev.bandb.graphview.graph.Edge
import dev.bandb.graphview.graph.Node

class OptionEdge(
    source: Node,
    destination: Node,
    val option: Option
) : Edge(source, destination)