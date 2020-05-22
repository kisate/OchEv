package com.example.ochev.baseclasses.dataclasses.edgefigures

import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure


// Figure that connects information blocks

data class Edge(
    val beginFigure: VertexFigure,
    val endFigure: VertexFigure
) : Figure() {
    val beginPoint: Point
        get() {
            return beginFigure.getMovingPoints().minBy {
                it.getDistanceToPoint(endFigure.center)
            }!!
        }
    val endPoint: Point
        get() {
            return endFigure.getMovingPoints().minBy {
                it.getDistanceToPoint(beginFigure.center)
            }!!
        }

    override val center: Point
        get() = Point(
            (beginPoint.x + endPoint.x) / 2,
            (beginPoint.y + endPoint.y) / 2
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
        return beginFigure.center.getDistanceToPoint(endFigure.center) / 4f
    }

}