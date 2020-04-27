package com.example.ochev.baseclasses.dataclasses

import java.lang.Float.min
import kotlin.math.sqrt

data class Point(var x: Int = 0, var y: Int = 0) {
    fun moveByVector(vector: Vector) {
        x += vector.x
        y += vector.y
    }

    fun getDistanceToLineSegment(
        linePointA: Point,
        linePointB: Point
    ): Float {
        // we have triange on 3 vertexes : A,B lies on line segment, С ( this ) is alone
        val vectorInteractor = VectorInteractor()
        val pointInteractor = PointInteractor()

        val vectorFromAToC = Vector(this.x - linePointA.x, this.y - linePointA.y)
        val vectorFromAToB = Vector(linePointB.x - linePointA.x, linePointB.y - linePointA.y)
        val vectorFromBToA = Vector(linePointA.x - linePointB.x, linePointA.y - linePointB.y)
        val vectorFromBToC = Vector(this.x - linePointB.x, this.y - linePointB.y)

        val signOfCosAngleCAB =
            if (vectorInteractor.scalarProduct(vectorFromAToB, vectorFromAToC) >= 0) 1 else -1
        val signOfCosAngleCBA =
            if (vectorInteractor.scalarProduct(vectorFromBToA, vectorFromBToC) >= 0) 1 else -1


        if (signOfCosAngleCAB == signOfCosAngleCBA) {

            // the shortest path is perpendicular

            val cosAngleCAB = vectorInteractor.scalarProduct(vectorFromAToB, vectorFromAToC) /
                    (vectorFromAToB.length *
                            vectorFromAToC.length)
            val sinAngleCAB = sqrt(1 - cosAngleCAB * cosAngleCAB)
            return vectorFromAToC.length * sinAngleCAB
        }

        return min(
            pointInteractor.distance(this, linePointA),
            pointInteractor.distance(this, linePointB)
        )
    }

}


class PointInteractor {

    fun distance(firstPoint: Point, secondPoint: Point): Float {
        return sqrt(
            (firstPoint.x - secondPoint.x) * (firstPoint.x - secondPoint.x) +
                    (firstPoint.y - secondPoint.y) * (firstPoint.y - secondPoint.y).toFloat()
        )
    }


}