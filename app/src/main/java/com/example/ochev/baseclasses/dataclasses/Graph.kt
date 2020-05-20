package com.example.ochev.baseclasses.dataclasses

import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.edgefigures.Edge
import com.example.ochev.baseclasses.vertexfigures.VertexFigure
import kotlin.math.abs


data class Graph(
    val figures: FigureContainer = FigureContainer()
) {


    fun getFigureForEditing(point: Point): Figure? {
        val bestFigure = getClosestToPointFigureOrNull(point)
        return if (bestFigure == null || !bestFigure.checkIfFigureIsCloseEnough(point)) null
        else bestFigure
    }


    fun getClosestToPointVertexFigureOrNull(point: Point): VertexFigure? {
        if (figures.vertexes.isEmpty()) return null

        val bestDist = figures.vertexes.minBy {
            it.first.getDistanceToPointOrZeroIfInside(point)
        }!!.first.getDistanceToPointOrZeroIfInside(point)

        val lookFor = figures.vertexes.partition {
            abs(it.first.getDistanceToPointOrZeroIfInside(point) - bestDist) <= 0.0001
        }.first
        return lookFor.maxBy { it.second }!!.first
    }

    fun getClosestToPointEdgeFigureOrNull(point: Point): Edge? {
        if (figures.edges.isEmpty()) return null

        val bestDist = figures.edges.minBy {
            it.first.getDistanceToPoint(point)
        }!!.first.getDistanceToPoint(point)

        val lookFor = figures.edges.partition {
            abs(it.first.getDistanceToPoint(point) - bestDist) <= 0.0001
        }.first
        return lookFor.maxBy { it.second }!!.first
    }

    fun getClosestToPointFigureOrNull(point: Point): Figure? {
        val bestVertex = getClosestToPointVertexFigureOrNull(point)
        val bestEdge = getClosestToPointEdgeFigureOrNull(point)

        if (bestVertex == null) return bestEdge
        if (bestEdge == null) return bestVertex

        if (bestVertex.checkIfPointIsInside(point)) return bestVertex

        return if (bestVertex.getDistanceToPointOrZeroIfInside(point) <= bestEdge.getDistanceToPoint(
                point
            )
        ) bestVertex
        else bestEdge
    }


}