package com.example.ochev.baseclasses.vertexfigures

import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.Stroke.Companion.getStrokesRestrictions
import com.example.ochev.baseclasses.dataclasses.Vector
import com.example.ochev.baseclasses.editors.vertexeditor.PointMover
import kotlin.math.abs
import kotlin.math.max

data class Circle(
    override val center: Point = Point(),
    val radius: Int = 0
) : VertexFigure() {
    val leftPoint: Point
        get() = Point(center.x - radius, center.y)
    val upPoint: Point
        get() = Point(center.x, center.y + radius)
    val rightPoint: Point
        get() = Point(center.x + radius, center.y)
    val downPoint: Point
        get() = Point(center.x, center.y - radius)

    override fun getDistanceToPoint(point: Point): Float {
        return abs(
            center.getDistanceToPoint(point)
                    - radius
        )
    }

    override fun movedByVector(vector: Vector): Circle {
        return this.copy(center = center.movedByVector(vector))
    }

    override fun checkIfPointIsInside(point: Point): Boolean {
        return center.getDistanceToPoint(point) <= radius
    }

    override fun getPointMovers(): MutableList<PointMover> {
        val result: MutableList<PointMover> = ArrayList()
        val points = getMovingPoints()
        val moveFun = { point: Point ->
            this.copy(radius = center.getDistanceToPoint(point).toInt())
        }

        points.forEach {
            result.add(
                PointMover(
                    it,
                    moveFun
                )
            )
        }

        return result
    }

    override fun getMovingPoints(): MutableList<Point> {
        return mutableListOf(leftPoint, upPoint, rightPoint, downPoint)
    }

    override fun getDistanceToCountTouch(): Float {
        return max(radius / 4f, 20f)
    }

}

fun VertexFigureBuilder.buildCircle(strokes: MutableList<Stroke>): Circle {
    val (maxX, maxY, minX, minY) = getStrokesRestrictions(strokes)
    return Circle(
        center = Point((maxX + minX) / 2, (maxY + minY) / 2),
        radius = ((maxX - minX) + (maxY - minY)) / 4
    )
}
