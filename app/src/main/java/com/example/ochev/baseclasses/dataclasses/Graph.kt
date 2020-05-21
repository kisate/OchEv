package com.example.ochev.baseclasses.dataclasses

import com.example.ochev.baseclasses.dataclasses.nodes.EdgeNode
import com.example.ochev.baseclasses.dataclasses.nodes.FigureNode
import com.example.ochev.baseclasses.dataclasses.nodes.VertexFigureNode
import kotlin.math.abs


data class Graph(
    val figures: FigureContainer = FigureContainer()
) {

    fun copy(): Graph {
        return Graph(figures.copy())
    }

    fun getFigureForEditing(point: Point): FigureNode? {
        val bestFigure = getClosestToPointFigureOrNull(point)
        return if (bestFigure == null || !bestFigure.figure.checkIfFigureIsCloseEnough(point)) null
        else bestFigure
    }


    fun getClosestToPointVertexFigureOrNull(point: Point): VertexFigureNode? {
        if (figures.vertices.isEmpty()) return null

        val bestDist = figures.vertices.minBy {
            it.figure.getDistanceToPointOrZeroIfInside(point)
        }!!.figure.getDistanceToPointOrZeroIfInside(point)

        val lookFor = figures.vertices.partition {
            abs(it.figure.getDistanceToPointOrZeroIfInside(point) - bestDist) <= 0.0001
        }.first
        return lookFor.maxBy { it.height }!!
    }

    fun getClosestToPointEdgeFigureOrNull(point: Point): EdgeNode? {
        return figures.edges.minBy {
            it.figure.getDistanceToPoint(point)
        }
    }

    fun getClosestToPointFigureOrNull(point: Point): FigureNode? {
        val bestVertex = getClosestToPointVertexFigureOrNull(point)
        val bestEdge = getClosestToPointEdgeFigureOrNull(point)

        if (bestVertex == null) return bestEdge
        if (bestEdge == null) return bestVertex

        if (bestVertex.figure.checkIfPointIsInside(point)) return bestVertex

        return if (bestVertex.figure.getDistanceToPointOrZeroIfInside(point)
            <= bestEdge.figure.getDistanceToPoint(point)
        ) bestVertex
        else bestEdge
    }


}