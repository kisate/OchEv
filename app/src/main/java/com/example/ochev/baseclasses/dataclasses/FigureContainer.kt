package com.example.ochev.baseclasses.dataclasses

import com.example.ochev.baseclasses.EdgeFigure
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.VertexFigure
import kotlin.math.max

data class FigureContainer(
    val vertexes: MutableList<Pair<VertexFigure, Int>> = ArrayList(),
    val edges: MutableList<Pair<EdgeFigure, Int>> = ArrayList()
) {
    val allFigures: MutableList<Pair<Figure, Int>>
        get() = (vertexes + edges).toMutableList()

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

    fun addVertex(vertex: VertexFigure, height: Int) {
        vertexes.add(Pair(vertex, height))
    }

    fun addEdge(edge: EdgeFigure, height: Int) {
        edges.add(Pair(edge, height))
    }


}