package com.example.ochev.baseclasses.dataclasses

import com.example.ochev.baseclasses.EdgeFigure
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.FigureNormalizer
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
        return vertexes.minBy {
            if (it.checkIfPointIsInside(point)) 0f
            else it.getDistanceToPoint(point)
        }
    }

    fun modifyByStrokes(information: InfrormationForNormalizer): Figure? {
        val normalizer = FigureNormalizer()
        val newFigure = normalizer.normaliseStrokes(information) ?: return null

        when (newFigure) {
            is VertexFigure -> addVertex(newFigure)
            is EdgeFigure -> addEdge(newFigure)
        }
        return newFigure
    }
}