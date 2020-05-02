package com.example.ochev.baseclasses.dataclasses

import com.example.ochev.baseclasses.EdgeFigure
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.FigureNormalizer
import com.example.ochev.baseclasses.VertexFigure


data class Graph(
    val vertexes: MutableList<VertexFigure> = ArrayList(),
    val edges: MutableList<EdgeFigure> = ArrayList()
) {
    val allFigures: MutableList<Figure>
        get() = (vertexes + edges).toMutableList()

    class ComparatorByHeights() : Comparator<Figure> {
        override fun compare(x: Figure?, y: Figure?): Int {
            if (x == null && y == null) return 0
            if (x == null) return -1
            if (y == null) return 1

            val diff = x.heightOnPlain - y.heightOnPlain
            if (diff == 0) {
                if (x is VertexFigure && y is EdgeFigure) return 1
                if (x is EdgeFigure && y is VertexFigure) return -1
            }
            return diff
        }

    }

    val figuresSortedByHeights: MutableList<Figure>
        get() = allFigures.sortedWith(ComparatorByHeights()).toMutableList()

    fun addEdge(edgeFigure: EdgeFigure) {
        edges.add(edgeFigure)
    }

    fun addVertex(vertexFigure: VertexFigure) {
        vertexes.add(vertexFigure)
    }

    fun getClosestToPointVertexFigureOrNull(point: Point): VertexFigure? {
        return vertexes.sortedByDescending { it.heightOnPlain }.minBy {
            it.getDistanceToPointOrZeroIfInside(point)
        }
    }

    fun getClosestToPointEdgeFigureOrNull(point: Point): EdgeFigure? {
        return edges.sortedByDescending { it.heightOnPlain }.minBy {
            it.getDistanceToPoint(point)
        }
    }

    fun getClosestFigureToPointOrNull(point: Point): Figure? {
        val bestVertex = getClosestToPointVertexFigureOrNull(point)
        val bestEdge = getClosestToPointEdgeFigureOrNull(point)

        if (bestVertex == null) return bestEdge
        if (bestEdge == null) return bestVertex

        return if (bestVertex.getDistanceToPointOrZeroIfInside(point) <= 0.0001) {
            bestVertex
        } else {
            if (
                bestVertex.getDistanceToPointOrZeroIfInside(point) <=
                bestEdge.getDistanceToPoint(point)
            ) bestVertex
            else bestEdge
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