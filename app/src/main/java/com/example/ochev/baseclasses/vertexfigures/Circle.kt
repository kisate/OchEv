package com.example.ochev.baseclasses.vertexfigures

import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.Stroke.Companion.getStrokesRestrictions
import com.example.ochev.baseclasses.dataclasses.Vector
import com.example.ochev.baseclasses.editors.vertexeditor.PointMover
import kotlin.math.abs
import kotlin.math.max

data class Circle(
    override val center: Point = Point(),
    val radius: Float = 0f
) : VertexFigure() {
    val leftPoint: Point
        get() = Point(center.x - radius, center.y)
    val upPoint: Point
        get() = Point(center.x, center.y + radius)
    val rightPoint: Point
        get() = Point(center.x + radius, center.y)
    val downPoint: Point
        get() = Point(center.x, center.y - radius)

    override val importantPoints: MutableList<Point>
        get() {
            return mutableListOf(leftPoint, upPoint, rightPoint, downPoint)
        }

    override fun rescaledByFactor(factor: Float): VertexFigure {
        return this.copy(radius = radius * factor)
    }

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
            this.copy(radius = center.getDistanceToPoint(point))
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
        return importantPoints
    }

    override fun getDistanceToCountTouch(): Float {
        return max(radius / 3.5f, 20f)
    }

}

fun VertexFigureBuilder.buildCircle(strokes: MutableList<Stroke>): Circle {
    val (maxX, maxY, minX, minY) = getStrokesRestrictions(strokes)
    return Circle(
        center = Point((maxX + minX) / 2, (maxY + minY) / 2),
        radius = ((maxX - minX) + (maxY - minY)) / 4
    )
}
