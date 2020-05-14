package com.example.ochev.baseclasses

import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Vector
import com.example.ochev.baseclasses.editors.vertexeditor.PointMover

/*
A figure, that represents an information block in our scheme
 */

abstract class VertexFigure : Figure() {
    abstract val center: Point

    abstract fun movedByVector(vector: Vector): VertexFigure
    abstract fun checkIfPointIsInside(point: Point): Boolean
    abstract fun getPointMovers(): MutableList<PointMover>
    abstract fun getMovingPoints(): MutableList<Point>

    fun getDistanceToPointOrZeroIfInside(point: Point): Float {
        return if (checkIfPointIsInside(point)) 0f
        else getDistanceToPoint(point)
    }

    override fun checkIfFigureIsCloseEnough(point: Point): Boolean {
        return getDistanceToPointOrZeroIfInside(point) <= getDistanceToCountTouch()
    }
}




