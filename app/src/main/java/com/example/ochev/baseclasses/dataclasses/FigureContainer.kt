package com.example.ochev.baseclasses.dataclasses

import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.dataclasses.nodes.EdgeNode
import com.example.ochev.baseclasses.dataclasses.nodes.FigureNode
import com.example.ochev.baseclasses.dataclasses.nodes.VertexFigureNode
import com.example.ochev.baseclasses.edgefigures.Edge
import com.example.ochev.baseclasses.vertexfigures.VertexFigure
import kotlin.math.max

data class FigureContainer(
    val vertices: MutableList<VertexFigureNode> = ArrayList(),
    val edges: MutableList<EdgeNode> = ArrayList()
) {

    class ComparatorByHeights : Comparator<FigureNode> {
        override fun compare(o1: FigureNode?, o2: FigureNode?): Int {
            if (o1 == null && o2 == null) return 0
            if (o1 == null) return -1
            if (o2 == null) return 1

            if (o1.height != o2.height) {
                return o1.height - o2.height
            }

            if (o1.figure is VertexFigure && o2.figure is Edge) return 1
            if (o2.figure is Edge && o2.figure is VertexFigure) return -1
            return 0
        }
    }

    val figuresSortedByHeights: MutableList<FigureNode>
        get() = (vertices + edges).sortedWith(ComparatorByHeights()).toMutableList()

    val maxHeightVertex: Int
        get() {
            return vertices.maxBy { it.height }?.height ?: 0
        }

    val maxHeightEdge: Int
        get() {
            return edges.maxBy { it.height }?.height ?: 0
        }

    val maxHeight: Int
        get() {
            return max(maxHeightVertex, maxHeightEdge)
        }

    fun getHeight(figure: Figure): Int {
        return when (figure) {
            is Edge -> edges.first { it.figure == figure }.height
            is VertexFigure -> vertices.first { it.figure == figure }.height
            else -> 0
        }
    }
}