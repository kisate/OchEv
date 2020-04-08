package baseclasses

class Graph(
    val vertexes: MutableList<VertexFigure> = ArrayList(),
    val edges: MutableList<EdgeFigure> = ArrayList()
) {
    fun addEdge(new_edge: EdgeFigure) {
        edges.add(new_edge)
    }

    fun addVertex(new_vertex: VertexFigure) {
        vertexes.add(new_vertex)
    }
}