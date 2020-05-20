package com.example.ochev.baseclasses

import com.example.ochev.baseclasses.dataclasses.Point

// Base object of our scheme

abstract class Figure {
    abstract fun getDistanceToPoint(point: Point): Float
    abstract fun getDistanceToCountTouch(): Float
    abstract fun checkIfFigureIsCloseEnough(point: Point): Boolean
}




