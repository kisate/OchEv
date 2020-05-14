package com.example.ochev.baseclasses.dataclasses

import android.view.MotionEvent
import kotlin.math.min
import kotlin.math.sqrt

data class Point(val x: Int = 0, val y: Int = 0) {

    constructor(event: MotionEvent) : this(event.x.toInt(), event.y.toInt())

    fun getDistanceToPoint(point: Point): Float {
        return sqrt(((x - point.x) * (x - point.x) + (y - point.y) * (y - point.y)).toFloat())
    }

    fun movedByVector(vector: Vector): Point {
        return Point(x + vector.x, y + vector.y)
    }

    fun getDistanceToLineSegment(
        linePointA: Point,
        linePointB: Point
    ): Float {
        // we have triangle on 3 vertexes : A,B lies on line segment, С ( this ) is alone

        val vectorFromAToC = Vector(this.x - linePointA.x, this.y - linePointA.y)
        val vectorFromAToB = Vector(linePointB.x - linePointA.x, linePointB.y - linePointA.y)
        val vectorFromBToA = Vector(linePointA.x - linePointB.x, linePointA.y - linePointB.y)
        val vectorFromBToC = Vector(this.x - linePointB.x, this.y - linePointB.y)

        val signOfCosAngleCAB =
            if (vectorFromAToB.scalarProduct(vectorFromAToC) >= 0) 1 else -1
        val signOfCosAngleCBA =
            if (vectorFromBToA.scalarProduct(vectorFromBToC) >= 0) 1 else -1


        if (signOfCosAngleCAB == signOfCosAngleCBA) {

            // the shortest path is perpendicular

            val cosAngleCAB = vectorFromAToB.scalarProduct(vectorFromAToC) /
                    (vectorFromAToB.length * vectorFromAToC.length)
            val sinAngleCAB = sqrt(1 - cosAngleCAB * cosAngleCAB)
            return vectorFromAToC.length * sinAngleCAB
        }

        return min(
            getDistanceToPoint(linePointA),
            getDistanceToPoint(linePointB)
        )
    }
}
