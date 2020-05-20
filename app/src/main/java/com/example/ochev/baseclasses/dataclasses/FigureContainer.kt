package com.example.ochev.baseclasses.dataclasses

import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.edgefigures.Edge
import kotlin.math.max

data class FigureContainer(
    val vertexes: MutableList<Pair<VertexFigure, Int>> = ArrayList(),
    val edges: MutableList<Pair<Edge, Int>> = ArrayList()
) {

    class ComparatorByHeights : Comparator<Pair<Figure, Int>> {
        override fun compare(o1: Pair<Figure, Int>?, o2: Pair<Figure, Int>?): Int {
            if (o1 == null && o2 == null) return 0
            if (o1 == null) return -1
            if (o2 == null) return 1

            if (o1.second != o2.second) {
                return o1.second - o2.second
            }

            if (o1.first is VertexFigure && o2.first is Edge) return 1
            if (o2.first is Edge && o2.first is VertexFigure) return -1
            return 0
        }
    }

    val figuresSortedByHeights: MutableList<Pair<Figure, Int>>
        get() = (vertexes + edges).sortedWith(ComparatorByHeights()).toMutableList()

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
            is Edge -> edges.first { it.first == figure }.second
            is VertexFigure -> vertexes.first { it.first == figure }.second
            else -> 0
        }
    }

    fun addVertex(vertex: VertexFigure, height: Int) {
        vertexes.add(Pair(vertex, height))
    }

    fun addEdge(edge: Edge, height: Int) {
        edges.add(Pair(edge, height))
    }


}