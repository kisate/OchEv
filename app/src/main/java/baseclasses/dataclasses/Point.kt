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
}