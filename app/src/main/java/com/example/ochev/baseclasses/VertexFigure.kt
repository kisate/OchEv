package com.example.ochev.baseclasses

import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Vector

/*
A figure, that represents an information block in our scheme
 */

abstract class VertexFigure(
) : Figure() {
    abstract val center: Point

    abstract fun moveByVector(vector: Vector)
    abstract fun getDistanceToPoint(point: Point): Float
    abstract fun checkIfPointIsInside(point: Point): Boolean

    fun getDistanceToPointOrZeroIfInside(point: Point): Float {
        return if (checkIfPointIsInside(point)) 0f
        else getDistanceToPoint(point)
    }
}




