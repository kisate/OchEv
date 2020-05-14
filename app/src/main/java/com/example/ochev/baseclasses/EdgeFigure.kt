package com.example.ochev.baseclasses

import com.example.ochev.baseclasses.dataclasses.Point


// Figure that connects information blocks

abstract class EdgeFigure(
    val beginFigure: VertexFigure,
    val endFigure: VertexFigure
) : Figure() {
    override fun checkIfFigureIsCloseEnough(point: Point): Boolean {
        return getDistanceToPoint(point) <= getDistanceToCountTouch()
    }
}