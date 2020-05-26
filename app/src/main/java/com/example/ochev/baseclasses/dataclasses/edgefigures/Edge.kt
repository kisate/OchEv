package com.example.ochev.baseclasses.dataclasses.edgefigures

import android.util.Log
import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.LineSegment
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure


// Figure that connects information blocks

data class Edge(
    val beginFigure: VertexFigure,
    val endFigure: VertexFigure
) : Figure() {
    val realBeginPoint: Point?
        get() {
            beginFigure.getIntersectionWithLineSegment(
                LineSegment(
                    beginFigure.center,
                    endFigure.center
                )
            ).let {
                Log.i("EdgeDebugBegin", it.toString())
                return if (it.size >= 1) it.first()
                else null
            }
        }

    val realEndPoint: Point?
        get() {
            endFigure.getIntersectionWithLineSegment(
                LineSegment(beginFigure.center, endFigure.center)
            ).let {
                Log.i("EdgeDebugEnd", it.toString())
                return if (it.size >= 1) it.first()
                else null
            }
        }


    override val center: Point
        get() = Point(
            (beginFigure.center.x + endFigure.center.x) / 2,
            (beginFigure.center.y + endFigure.center.y) / 2
        )

    override fun checkIfFigureIsCloseEnough(point: Point): Boolean {
        return getDistanceToPoint(point) <= getDistanceToCountTouch()
    }

    override fun getDistanceToPoint(point: Point): Float {
        return point.getDistanceToLineSegment(
            LineSegment(beginFigure.center, endFigure.center)
        )
    }

    override fun getDistanceToCountTouch(): Float {
        return 40f
    }

    // returns 0 if begin is closer, 1 otherwise, -1 if both points are far away

    fun getIndexOfClosestEnd(point: Point): Int {
        val checker = { it: Point ->
            it.getDistanceToPoint(point) <= this.getDistanceToCountTouch()
        }

        if (realBeginPoint == null || realEndPoint == null) return -1

        return if (realBeginPoint!!.getDistanceToPoint(point) <= realEndPoint!!.getDistanceToPoint(
                point
            )
        ) {
            if (checker(realBeginPoint!!)) 0
            else -1
        } else {
            if (checker(realEndPoint!!)) 1
            else -1
        }
    }

}