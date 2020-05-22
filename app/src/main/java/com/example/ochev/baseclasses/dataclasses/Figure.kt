package com.example.ochev.baseclasses.dataclasses

// Base object of our scheme

abstract class Figure {
    abstract val center: Point
    abstract fun getDistanceToPoint(point: Point): Float
    abstract fun getDistanceToCountTouch(): Float
    abstract fun checkIfFigureIsCloseEnough(point: Point): Boolean
}




