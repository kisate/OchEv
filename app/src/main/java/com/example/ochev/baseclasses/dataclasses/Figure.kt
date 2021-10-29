package com.example.ochev.baseclasses.dataclasses

// Base object of our scheme

abstract class Figure {
    abstract val center: Point
    abstract fun getDistanceToPoint(point: Point): Float
    abstract fun getDistanceToCountTouch(): Float
    abstract fun checkIfFigureIsCloseEnough(point: Point): Boolean

    abstract fun getFigureId(): FIGURE_ID
}

enum class FIGURE_ID(val order: Int) {
    CIRCLE(0),
    RECTANGLE(1),
    RHOMBUS(2),
    EDGE(3),
}


