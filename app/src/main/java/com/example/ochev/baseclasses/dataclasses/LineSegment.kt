package com.example.ochev.baseclasses.dataclasses

data class LineSegment(
    val A: Point,
    val B: Point
) {
    fun toVector(): Vector {
        return Vector(B.x - A.x, B.y - A.y)
    }
}