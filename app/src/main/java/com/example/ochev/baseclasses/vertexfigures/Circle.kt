package com.example.ochev.baseclasses.vertexfigures

import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.*
import com.example.ochev.baseclasses.vertexfigures.editors.PointMover
import kotlin.math.abs

class Circle(
    override val center: Point = Point(),
    var radius: Int = 0,
    val leftPoint: Point = Point(center.x - radius, center.y),
    val upPoint: Point = Point(center.x, center.y + radius),
    val rightPoint: Point = Point(center.x + radius, center.y),
    val downPoint: Point = Point(center.x, center.y - radius)
) : VertexFigure() {
    override fun getDistanceToPoint(point: Point): Float {
        val pointInteractor = PointInteractor()
        return abs(
            pointInteractor.distance(center, point)
                    - radius
        )
    }

    override fun moveByVector(vector: Vector) {
        center.x += vector.x
        center.y += vector.y
    }

    override fun checkIfPointIsInside(point: Point): Boolean {
        val pointInteractor = PointInteractor()
        return pointInteractor.distance(point, center) <= radius
    }

    override fun getPointMovers(): MutableList<PointMover> {
        val points = listOf(leftPoint, upPoint, rightPoint, downPoint)

        val result: MutableList<PointMover> = ArrayList()

        for (i in 0..3) {
            val moveFun = { point: Point ->
                val pointInteractor = PointInteractor()
                val newRadius = pointInteractor.distance(center, point)
                radius = newRadius.toInt()
            }

            result.add(PointMover(points[i], moveFun))
        }

        return result
    }
}

fun VertexFigureBuilder.buildCircle(strokes: MutableList<Stroke>): Circle {
    val strokeInteractor = StrokeInteractor()
    val (maxX, maxY, minX, minY) = strokeInteractor.getStrokesRestrictions(strokes)
    return Circle(
        center = Point((maxX + minX) / 2, (maxY + minY) / 2),
        radius = ((maxX - minX) + (maxY - minY)) / 4
    )
}
