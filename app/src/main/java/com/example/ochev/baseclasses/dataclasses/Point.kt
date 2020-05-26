package com.example.ochev.baseclasses.dataclasses

import android.util.Log
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
        fun centre(arr: MutableList<Point>): Point {
            var ret = Point()
            for (point in arr) {
                ret = Point(ret.x + point.x, ret.y + point.y)
            }
            ret = Point(ret.x / arr.size, ret.y / arr.size)
            return ret
        }

        fun intersectTwoSegments(p1: Point, p2: Point, p3: Point, p4: Point): Point? {
            Log.i("intersection.pro", p1.toString() + " " + p2.toString() + " " + p3.toString() + " " + p4.toString() )
            val v12 = Vector(p1, p2)
            val v34 = Vector(p3, p4)

            if (abs(v12.vectorProduct(v34)) <= 0.001f){
                return null
            }

            Log.i("intersection.pro.dbg2", (p1.x*v12.y + p3.y*v12.x - p1.y*v12.x - p3.x*v12.y).toString() )

            Log.i("intersection.pro.dbg3", (v34.x*v12.y - v34.y*v12.x).toString() )

            val b = (p1.x*v12.y + p3.y*v12.x - p1.y*v12.x - p3.x*v12.y) / (v34.x*v12.y - v34.y*v12.x)

            Log.i("intersection.pro.dbg4", b.toString() )


            val interestingPoint = Point(p3.x + v34.x * b, p3.y + v34.y * b)

            if (abs(p1.getDistanceToPoint(p2) - interestingPoint.getDistanceToPoint(p1) - interestingPoint.getDistanceToPoint(p2)) <= 0.001f &&
                abs(p3.getDistanceToPoint(p4) - interestingPoint.getDistanceToPoint(p3) - interestingPoint.getDistanceToPoint(p4)) <= 0.001f) {
                return interestingPoint
            }
            return null
        }
    }
}
