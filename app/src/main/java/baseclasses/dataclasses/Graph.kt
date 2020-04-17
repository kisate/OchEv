package baseclasses.dataclasses

import baseclasses.EdgeFigure
import baseclasses.VertexFigure

data class Graph(
    val vertexes: MutableList<VertexFigure> = ArrayList(),
    val edges: MutableList<EdgeFigure> = ArrayList()
)

fun Graph.addEdge(edgeToAdd: EdgeFigure) {
    edges.add(edgeToAdd)
}

fun Graph.addVertex(vertexToAdd: VertexFigure) {
    vertexes.add(vertexToAdd)
}