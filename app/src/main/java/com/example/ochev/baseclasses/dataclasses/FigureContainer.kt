package com.example.ochev.baseclasses.dataclasses

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.ochev.baseclasses.EdgeFigure
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.edgefigures.Line
import kotlin.math.max

data class FigureContainer(
    val vertexes: MutableList<Pair<VertexFigure, Int>> = ArrayList(),
    val edges: MutableList<Pair<EdgeFigure, Int>> = ArrayList()
) {

    val maxHeightVertex: Int
        get() {
            return vertexes.maxBy { it.second }?.second ?: 0
        }

    val maxHeightEdge: Int
        get() {
            return edges.maxBy { it.second }?.second ?: 0
        }

    val maxHeight: Int
        get() {
            return max(maxHeightVertex, maxHeightEdge)
        }

    fun getHeight(figure: Figure): Int {
        return when (figure) {
            is EdgeFigure -> edges.first { it.first == figure }.second
            is VertexFigure -> vertexes.first { it.first == figure }.second
            else -> 0
        }
    }

    fun recalcEdgeHeights() {
        val fastHeightAcces: HashMap<VertexFigure, Int> = HashMap()
        vertexes.forEach { fastHeightAcces[it.first] = it.second }

        val realEdges: MutableList<Pair<EdgeFigure, Int>> = ArrayList()
        edges.forEach {
            realEdges.add(
                Pair(
                    it.first, kotlin.math.min(
                        fastHeightAcces[it.first.beginFigure]!!,
                        fastHeightAcces[it.first.endFigure]!!
                    )
                )
            )
        }

        edges.clear()
        edges += realEdges
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun replaceVertex(old: VertexFigure, new: VertexFigure) {
        // redirecting edges
        val edgesToAdd: MutableList<Pair<EdgeFigure, Int>> = ArrayList()
        edges.forEach {
            if (it.first.beginFigure == old) {
                when (it.first) {
                    is Line -> edgesToAdd.add(Pair(Line(new, it.first.endFigure), it.second))
                }
            }
            if (it.first.endFigure == old) {
                when (it.first) {
                    is Line -> edgesToAdd.add(Pair(Line(it.first.beginFigure, new), it.second))
                }
            }
        }

        edges.removeIf { it.first.beginFigure == old || it.first.endFigure == old }
        edges += edgesToAdd

        val height = getHeight(old)

        vertexes.remove(Pair(old, height))
        vertexes.add(Pair(new, height))
    }


    fun addVertex(vertex: VertexFigure, height: Int) {
        vertexes.add(Pair(vertex, height))
    }

    fun addEdge(edge: EdgeFigure, height: Int) {
        edges.add(Pair(edge, height))
    }

    fun addFigure(figure: Figure, height: Int) {
        when (figure) {
            is VertexFigure -> addVertex(figure, height)
            is EdgeFigure -> addEdge(figure, height)
        }
    }

    fun getClosestToPointVertexFigureOrNull(point: Point): VertexFigure? {
        if (vertexes.isEmpty()) return null

        val maxHeight = vertexes.maxBy { it.second }!!.second
        val lookFor = vertexes.partition { it.second == maxHeight }.first
        return lookFor.minBy { it.first.getDistanceToPointOrZeroIfInside(point) }!!.first
    }

    fun getClosestToPointEdgeFigureOrNull(point: Point): EdgeFigure? {
        if (edges.isEmpty()) return null

        val maxHeight = edges.maxBy { it.second }!!.second
        val lookFor = edges.partition { it.second == maxHeight }.first
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