package com.example.ochev.baseclasses.editors.vertexeditor

import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.Point

class PointMover(
    val point: Point,
    val moveFun: (point: Point) -> VertexFigure
) {
    fun pointToMove(point: Point) {
        moveFun(point)
    }

}