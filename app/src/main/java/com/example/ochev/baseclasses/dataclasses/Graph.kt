package com.example.ochev.baseclasses.dataclasses

import com.example.ochev.baseclasses.EdgeFigure
import com.example.ochev.baseclasses.VertexFigure

data class Graph(
    val vertexes: MutableList<VertexFigure> = ArrayList(),
    val edges: MutableList<EdgeFigure> = ArrayList()
) {
    fun addEdge(edgeFigure: EdgeFigure) {
        edges.add(edgeFigure)
    }

    fun addVertex(vertexFigure: VertexFigure) {
        vertexes.add(vertexFigure)
    }

    fun getClosestToPointVertexFigureOrNull(point: Point): VertexFigure? {
        return vertexes.minBy { it.getDistanceToPoint(point) }
    }
}
