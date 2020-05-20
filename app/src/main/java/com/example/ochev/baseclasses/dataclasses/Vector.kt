package com.example.ochev.baseclasses.dataclasses

import kotlin.math.sqrt

data class Vector(val x: Float = 0f, val y: Float = 0f) {
    val length: Float
        get() = sqrt((x * x) + (y * y))

    constructor(beginPoint: Point, endPoint: Point) : this(
        endPoint.x - beginPoint.x,
        endPoint.y - beginPoint.y
    )

    fun scalarProduct(vector: Vector): Float {
        return x * vector.x + y * vector.y
    }

    fun vectorProduct(vector: Vector): Float {
        return x * vector.y - y * vector.x
    }

    fun multipliedByFloat(c: Float): Vector {
        return Vector(x * c, y * c)
    }
}
