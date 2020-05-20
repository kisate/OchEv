package com.example.ochev.baseclasses.dataclasses

import kotlin.math.sqrt

data class Vector(val x: Int = 0, val y: Int = 0) {
    val length: Float
        get() = sqrt(((x * x) + (y * y)).toFloat())

    constructor(beginPoint: Point, endPoint: Point) : this(
        endPoint.x - beginPoint.x,
        endPoint.y - beginPoint.y
    )

    fun scalarProduct(vector: Vector): Int {
        return x * vector.x + y * vector.y
    }

    fun vectorProduct(vector: Vector): Int {
        return x * vector.y - y * vector.x
    }

    fun multipliedByFloat(c: Float): Vector {
        return Vector((x * c).toInt(), (y * c).toInt())
    }
}
