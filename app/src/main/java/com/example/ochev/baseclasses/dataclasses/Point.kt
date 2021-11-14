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
        segment: LineSegment
    ): Float {
        // we have triangle on 3 vertexes : A,B lies on line segment, С ( this ) is alone
        val linePointA = segment.A
        val linePointB = segment.B

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

    companion object {
        fun centre(arr: MutableList<Point>): Point {
            var ret = Point()
            for (point in arr) {
                ret = Point(ret.x + point.x, ret.y + point.y)
            }
            ret = Point(ret.x / arr.size, ret.y / arr.size)
            return ret
        }

        private fun intersectTwoLines(
            firstSegment: LineSegment,
            secondSegment: LineSegment
        ): Point? {
            val p1 = firstSegment.A
            val p2 = firstSegment.B
            val p3 = secondSegment.A
            val p4 = secondSegment.B
            Log.i("intersection.pro", "$p1 $p2 $p3 $p4")
            val v12 = Vector(p1, p2)
            val v34 = Vector(p3, p4)

            if (abs(v12.vectorProduct(v34)) <= 0.001f) {
                return null
            }

            Log.i(
                "intersection.pro.dbg2",
                (p1.x * v12.y + p3.y * v12.x - p1.y * v12.x - p3.x * v12.y).toString()
            )

            Log.i("intersection.pro.dbg3", (v34.x * v12.y - v34.y * v12.x).toString())

            val b =
                (p1.x * v12.y + p3.y * v12.x - p1.y * v12.x - p3.x * v12.y) / (v34.x * v12.y - v34.y * v12.x)

            Log.i("intersection.pro.dbg4", b.toString())


            return Point(p3.x + v34.x * b, p3.y + v34.y * b)
        }

        fun intersectTwoSegments(firstSegment: LineSegment, secondSegment: LineSegment): Point? {
            val p1 = firstSegment.A
            val p2 = firstSegment.B
            val p3 = secondSegment.A
            val p4 = secondSegment.B
            val interestingPoint = intersectTwoLines(firstSegment, secondSegment)
            if (interestingPoint == null) return null
            if (abs(
                    p1.getDistanceToPoint(p2) - interestingPoint.getDistanceToPoint(p1) - interestingPoint.getDistanceToPoint(
                        p2
                    )
                ) <= 0.001f &&
                abs(
                    p3.getDistanceToPoint(p4) - interestingPoint.getDistanceToPoint(p3) - interestingPoint.getDistanceToPoint(
                        p4
                    )
                ) <= 0.001f
            ) {
                return interestingPoint
            }
            return null
        }

        private fun isPointInside(segment: LineSegment, point: Point): Boolean {
            val v1 = Vector(segment.A, segment.B)
            val v2 = Vector(segment.A, point)
            return if (abs(v1.vectorProduct(v2)) <= 0.00001f) {
                abs(
                    point.getDistanceToPoint(segment.A) + point.getDistanceToPoint(segment.B) - segment.A.getDistanceToPoint(
                        segment.B
                    )
                ) <= 0.0001f
            } else {
                false
            }
        }

        fun getOptimalSegment(segment: LineSegment, point: Point): LineSegment {
            val v1 = Vector(segment.A, segment.B)
            val v2 = Vector(segment.A, point)
            val v3 = Vector(v1.y, -v1.x)
            if (abs(v1.vectorProduct(v2)) <= 0.00001f) {
                return if (abs(
                        point.getDistanceToPoint(segment.A) + point.getDistanceToPoint(segment.B) - segment.A.getDistanceToPoint(
                            segment.B
                        )
                    ) <= 0.0001f
                ) {
                    LineSegment(point, point)
                } else {
                    if (point.getDistanceToPoint(segment.A) <= point.getDistanceToPoint(segment.B)) {
                        LineSegment(point, segment.A)
                    } else {
                        LineSegment(point, segment.B)
                    }
                }
            } else {
                val interestingPoint = intersectTwoLines(
                    LineSegment(point, Point(v3.x + point.x, v3.y + point.y)),
                    segment
                )
                if (isPointInside(segment, interestingPoint!!)) {
                    return LineSegment(point, interestingPoint)

                } else {
                    if (interestingPoint.getDistanceToPoint(segment.A) <= interestingPoint.getDistanceToPoint(
                            segment.B
                        )
                    ) {
                        return LineSegment(point, segment.A)
                    } else {
                        return LineSegment(point, segment.B)
                    }
                }
            }
        }
    }
}
