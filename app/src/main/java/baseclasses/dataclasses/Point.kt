package baseclasses.dataclasses

import java.lang.Float.min
import kotlin.math.sqrt

data class Point(var x: Int = 0, var y: Int = 0)


class PointInteractor {

    fun getDistanceBetweenTwoPoints(firstPoint: Point, secondPoint: Point): Float {
        return sqrt(
            (firstPoint.x - secondPoint.x) * (firstPoint.x - secondPoint.x) +
                    (firstPoint.y - secondPoint.y) * (firstPoint.y - secondPoint.y).toFloat()
        )
    }

    fun moveByVector(point: Point, direction: Vector) {
        point.x += direction.x
        point.y += direction.y
    }

    private fun pointDFS(
        currentPoint: Point,
        result: MutableList<Point>,
        deltas: List<Vector>,
        used: HashMap<Point, Boolean>,
        checker: (Point) -> Boolean
    ) {
        used[currentPoint] = true
        result.add(currentPoint)
        for (vector in deltas) {
            val newPoint = currentPoint.copy()
            moveByVector(newPoint, vector)
            if (checker(newPoint) && !used.containsKey(newPoint)) {
                pointDFS(newPoint, result, deltas, used, checker)
            }
        }
    }


    fun getPointsAroundPoint(startPoint: Point, radius: Float): MutableList<Point> {
        val result: MutableList<Point> = ArrayList()
        val deltas = listOf(Vector(1, 0), Vector(-1, 0), Vector(0, 1), Vector(0, -1))
        val used: HashMap<Point, Boolean> = HashMap()

        pointDFS(
            startPoint, result, deltas, used
        ) { getDistanceBetweenTwoPoints(it, startPoint) <= radius }
        return result
    }

    fun getPointsAroundLine(startPoint: Point, endPoint: Point, radius: Float): MutableList<Point> {
        val vectorInteractor = VectorInteractor()
        val result: HashMap<Point, Boolean> = HashMap()
        val vector = vectorInteractor.getVectorByTwoPoints(startPoint, endPoint)
        val length = vectorInteractor.getLength(vector)

        for (i in 0..length.toInt()) {
            val newPoint = startPoint.copy()
            moveByVector(
                newPoint,
                Vector((vector.x * i / length).toInt(), (vector.y * i / length).toInt())
            )
            for (point in getPointsAroundPoint(newPoint, radius)) {
                result[point] = true
            }
        }

        return result.keys.toMutableList()
    }


    fun getDistanceBetweenPointAndLineSegment(
        pointC: Point,
        linePointA: Point,
        linePointB: Point
    ): Float {
        // we have triange on 3 vertexes : A,B lies on line segment, C is alone
        val vectorInteractor = VectorInteractor()
        val pointInteractor = PointInteractor()

        val vectorFromAToC = Vector(pointC.x - linePointA.x, pointC.y - linePointA.x)
        val vectorFromAToB = Vector(linePointB.x - linePointA.x, linePointB.y - linePointA.y)
        val vectorFromBToA = Vector(linePointA.x - linePointB.x, linePointA.y - linePointB.y)
        val vectorFromBToC = Vector(pointC.x - linePointB.x, pointC.y - linePointB.y)

        val signOfCosAngleCAB =
            if (vectorInteractor.scalarProduct(vectorFromAToB, vectorFromAToC) >= 0) 1 else -1
        val signOfCosAngleCBA =
            if (vectorInteractor.scalarProduct(vectorFromBToA, vectorFromBToC) >= 0) 1 else -1


        if (signOfCosAngleCAB == 1 && signOfCosAngleCBA == 1) {

            // the shortest path is perpendicular

            val cosAngleCAB = vectorInteractor.scalarProduct(vectorFromAToB, vectorFromAToC) /
                    (vectorInteractor.getLength(vectorFromAToB) *
                            vectorInteractor.getLength(vectorFromAToC))
            val sinAngleCAB = sqrt(1 - cosAngleCAB * cosAngleCAB)
            return vectorInteractor.getLength(vectorFromAToC) * sinAngleCAB
        }

        return min(
            pointInteractor.getDistanceBetweenTwoPoints(pointC, linePointA),
            pointInteractor.getDistanceBetweenTwoPoints(pointC, linePointB)
        )
    }
}