package baseclasses.dataclasses

import kotlin.math.sqrt

data class Point(var x: Int = 0, var y: Int = 0, val time: Long = 0)


class PointInteractor {
    fun getDistanceBetweenTwoPoints(firstPoint: Point, secondPoint: Point): Double {
        return sqrt(
            (firstPoint.x - secondPoint.x) * (firstPoint.x - secondPoint.x) +
                    (firstPoint.y - secondPoint.y) * (firstPoint.y - secondPoint.y).toDouble()
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


    fun getPointsAroundPoint(startPoint: Point, radius: Double): MutableList<Point> {
        val result: MutableList<Point> = ArrayList()
        val deltas = listOf(Vector(1, 0), Vector(-1, 0), Vector(0, 1), Vector(0, -1))
        val used: HashMap<Point, Boolean> = HashMap()

        pointDFS(
            startPoint, result, deltas, used
        ) { getDistanceBetweenTwoPoints(it, startPoint) <= radius }
        return result
    }

    fun getPointsAroundLine(startPoint: Point, endPoint: Point, radius: Double) {
        val result: Set<Point>
    }
}