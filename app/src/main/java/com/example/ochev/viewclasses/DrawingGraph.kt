package com.example.ochev.viewclasses

import com.example.ochev.baseclasses.EdgeFigure
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.FigureNormalizer
import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.Graph
import com.example.ochev.baseclasses.dataclasses.InfrormationForNormalizer
import com.example.ochev.baseclasses.dataclasses.Point

enum class DrawingMode(value: Int) {
    EDITING(1),
    DEFAULT(2);
}

class DrawingEdgeFigure(val edgeFigure: EdgeFigure) {
    var drawingMode = DrawingMode.DEFAULT
}

class DrawingVertexFigure(val vertexFigure: VertexFigure) {
    var drawingMode = DrawingMode.DEFAULT
}

class DrawingGraph(
    val vertexes: MutableList<DrawingVertexFigure> = ArrayList(),
    val edges: MutableList<DrawingEdgeFigure> = ArrayList()
) {
    fun addEdge(edgeFigure: EdgeFigure) {
        edges.add(DrawingEdgeFigure(edgeFigure))
    }

    fun addVertex(vertexFigure: VertexFigure) {
        vertexes.add(DrawingVertexFigure(vertexFigure))
    }

    fun getClosestToPointVertexFigureOrNull(point: Point): VertexFigure? {
        return vertexes.minBy {
            if (it.vertexFigure.checkIfPointIsInside(point)) 0f
            else it.vertexFigure.getDistanceToPoint(point)
        }?.vertexFigure
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