package com.example.ochev.baseclasses.dataclasses.vertexfigures

import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.LineSegment
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Vector
import com.example.ochev.baseclasses.editors.vertexeditor.PointMover

/*
A figure, that represents an information block in our scheme
 */

abstract class VertexFigure : Figure() {

    abstract val importantPoints: MutableList<Point>

    abstract fun copy(): VertexFigure
    abstract fun getIntersectionWithLineSegment(segment: LineSegment): MutableList<Point>
    abstract fun rescaledByFactor(factor: Float): VertexFigure
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

    fun getLinesToHelpMoving(): MutableList<LineSegment> {
        return mutableListOf(
            LineSegment(Point(center.x, -5000f), Point(center.x, 5000f)),
            LineSegment(Point(-5000f, center.y), Point(5000f, center.y))
        )
    }
}




