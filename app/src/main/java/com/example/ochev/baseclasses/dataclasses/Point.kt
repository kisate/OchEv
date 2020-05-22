package com.example.ochev.baseclasses.dataclasses

import android.view.MotionEvent
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt

data class Point(val x: Float = 0f, val y: Float = 0f) {

    constructor(event: MotionEvent) : this(event.x, event.y)

    fun getDistanceToPoint(point: Point): Float {
        return sqrt((x - point.x) * (x - point.x) + (y - point.y) * (y - point.y))
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
            val sinAngleCAB = sqrt(1f - cosAngleCAB * cosAngleCAB)
            return vectorFromAToC.length * sinAngleCAB
        }

        return min(
            getDistanceToPoint(linePointA),
            getDistanceToPoint(linePointB)
        )
    }

    companion object{
        fun intersectTwoSegments(p1: Point, p2: Point, p3: Point, p4: Point): Point? {
            val v1 = Vector(p1, p2)
            val v2 = Vector(p3, p4)
            if (abs(v1.vectorProduct(v2)) <= 0.000001f){
                return null;
            }

            val b = (p1.x*v1.y + p3.x*v1.x - p1.y*v1.x - p3.x*v1.y) / (v2.x*v1.y - v2.y*v1.x)
            var a = 0f
            if (abs(v1.x) <= 0.000001f)a = (b*v2.x + p3.x - p1.x)/v1.x
            else a = (b*v2.y + p3.y - p1.y)/v1.y

            val interestingPoint = Point(p1.x + v1.x * a, p1.y + v1.y * a)

            if (abs(interestingPoint.getDistanceToPoint(p1) + interestingPoint.getDistanceToPoint(p2) - p1.getDistanceToPoint(p2)) <= 0.000001f ) {
                return interestingPoint
            }
            return null
        }
    }
}
