package com.example.ochev.baseclasses

import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.viewclasses.DrawingInformation

// Base object of our scheme

abstract class Figure(
    val drawingInformation: DrawingInformation = DrawingInformation(),
    var heightOnPlain: Int = 0
) {
    abstract fun getDistanceToPoint(point: Point): Float
}




