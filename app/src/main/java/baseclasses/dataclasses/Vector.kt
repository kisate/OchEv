package baseclasses.dataclasses

import kotlin.math.sqrt

data class Vector(var x: Int = 0, var y: Int = 0)

class VectorInteractor {
    fun getVectorByTwoPoints(beginPoint: Point, endPoint: Point): Vector {
        return Vector(endPoint.x - beginPoint.x, endPoint.y - beginPoint.y)
    }

    fun getLength(vector: Vector): Float {
        return sqrt((vector.x * vector.x + vector.y * vector.y).toFloat())
    }
}