package com.example.ochev.baseclasses.edgefigures

import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.vertexfigures.VertexFigure


// Figure that connects information blocks

data class Edge(
    val beginFigure: VertexFigure,
    val endFigure: VertexFigure
) : Figure() {
    override fun checkIfFigureIsCloseEnough(point: Point): Boolean {
        return getDistanceToPoint(point) <= getDistanceToCountTouch()
    }

    override fun getDistanceToPoint(point: Point): Float {
        return point.getDistanceToLineSegment(
            beginFigure.center,
            endFigure.center
        )
    }

    override fun getDistanceToCountTouch(): Float {
        return beginFigure.center.getDistanceToPoint(endFigure.center) / 4f
    }

}