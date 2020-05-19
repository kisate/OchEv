package com.example.ochev.baseclasses.dataclasses

import com.example.ochev.baseclasses.EdgeFigure
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.VertexFigure


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

        val maxHeight = figures.vertexes.maxBy { it.second }!!.second
        val lookFor = figures.vertexes.partition { it.second == maxHeight }.first
        return lookFor.minBy { it.first.getDistanceToPointOrZeroIfInside(point) }!!.first
    }

    fun getClosestToPointEdgeFigureOrNull(point: Point): EdgeFigure? {
        if (figures.edges.isEmpty()) return null

        val maxHeight = figures.edges.maxBy { it.second }!!.second
        val lookFor = figures.edges.partition { it.second == maxHeight }.first
        return lookFor.minBy { it.first.getDistanceToPoint(point) }!!.first
    }

    fun getClosestToPointFigureOrNull(point: Point): Figure? {
        val bestVertex = getClosestToPointVertexFigureOrNull(point)
        val bestEdge = getClosestToPointEdgeFigureOrNull(point)

        if (bestVertex == null) return bestEdge
        if (bestEdge == null) return bestVertex

        if (bestVertex.checkIfPointIsInside(point)) return bestVertex

        return if (bestVertex.getDistanceToPoint(point) <= bestEdge.getDistanceToPoint(point)) bestVertex
        else bestEdge
    }


}