package com.example.ochev.baseclasses

import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.viewclasses.DrawingInformation

// Base object of our scheme

abstract class Figure(
    val drawingInformation: DrawingInformation = DrawingInformation()
) {
    abstract fun getDistanceToPoint(point: Point): Float
    abstract fun getDistanceToCountTouch(): Float
    abstract fun checkIfFigureIsCloseEnough(point: Point): Boolean
}




