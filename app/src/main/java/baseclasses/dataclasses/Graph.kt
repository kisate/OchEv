package baseclasses.dataclasses

import baseclasses.EdgeFigure
import baseclasses.VertexFigure

data class Graph(
    val vertexes: MutableList<VertexFigure> = ArrayList(),
    val edges: MutableList<EdgeFigure> = ArrayList()
)

class graphInteractor {
    fun addEdge(graph: Graph, edgeFigure: EdgeFigure) {
        graph.edges.add(edgeFigure)
    }

    fun addVertex(graph: Graph, vertexFigure: VertexFigure) {
        graph.vertexes.add(vertexFigure)
    }
}