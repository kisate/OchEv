package com.example.ochev.baseclasses.vertexfigures.editors

import com.example.ochev.baseclasses.dataclasses.Point

class PointMover(
    val point: Point,
    val moveFun: (point: Point) -> Unit
) {
    fun pointToMove(point: Point) {
        moveFun(point)
    }

}