package com.example.ochev.baseclasses.dataclasses.edgefigures

import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure


// Figure that connects information blocks

data class Edge(
    val beginFigure: VertexFigure,
    val endFigure: VertexFigure
) : Figure() {


    override val center: Point
        get() = Point(
            (beginFigure.center.x + endFigure.center.x) / 2,
            (endFigure.center.y + endFigure.center.y) / 2
        )

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
        return 30f
    }

}