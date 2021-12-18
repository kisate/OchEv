package com.example.ochev.baseclasses.dataclasses.vertexfigures

import com.example.ochev.algorithms.QuadraticEquationSolver
import com.example.ochev.baseclasses.dataclasses.*
import com.example.ochev.baseclasses.dataclasses.Stroke.Companion.getStrokesRestrictions
import com.example.ochev.baseclasses.editors.vertexeditor.PointMover
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

data class Circle(
    override val center: Point = Point(),
    val radius: Float = 0f,
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

    override val drawingPoints: MutableList<Point>
        get() = mutableListOf(Point(leftPoint.x, upPoint.y), Point(rightPoint.x, downPoint.y))


    override fun clone(): VertexFigure {
        return this.copy()
    }


    override fun getIntersectionWithLineSegment(segment: LineSegment): MutableList<Point> {
        val result: MutableList<Point> = ArrayList()

        val (maxX, maxY, minX, minY) = getStrokesRestrictions(
            mutableListOf(Stroke(mutableListOf(segment.A, segment.B)))
        )

        if (maxX - minX <= 5) {
            val solutions = QuadraticEquationSolver.solveEquation(
                1f,
                -2 * center.y,
                maxX * maxX + center.x * center.x - 2 * maxX * center.x + center.y * center.y - radius * radius
            )
            solutions.forEach {
                if (it in minY..maxY) result.add(Point(maxX, it))
            }
            return result
        } else {
            // line is y = kx + d
            val k = (segment.A.y - segment.B.y) / (segment.A.x - segment.B.x)
            val d = segment.A.y - k * segment.A.x

            val solutions = QuadraticEquationSolver.solveEquation(
                (1 + k * k),
                -2 * center.x + 2 * k * d - 2 * k * center.y,
                center.x * center.x + d * d - 2 * d * center.y + center.y * center.y - radius * radius
            )
            solutions.forEach {
                if (it in minX..maxX && (k * it + d) in minY..maxY)
                    result.add(Point(it, k * it + d))
            }
            return result
        }
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

    private fun getMovingPoints(): MutableList<Point> {
        val res = mutableListOf<Point>()
        var curAngle = 0.0
        do {
            val curVec = Vector(cos(curAngle), sin(curAngle)).multipliedByFloat(radius)
            res.add(center.movedByVector(curVec))
            curAngle += 0.05f
        } while (curAngle < 2 * Math.PI)
        return res
    }

    override fun getDistanceToCountTouch(): Float {
        return max(radius / 3.33f, 27f)
    }

    override fun getFigureId(): FIGURE_ID {
        return FIGURE_ID.CIRCLE
    }

}

fun VertexFigureBuilder.buildCircle(strokes: MutableList<Stroke>): Circle {
    val (maxX, maxY, minX, minY) = getStrokesRestrictions(strokes)
    return Circle(
        center = Point((maxX + minX) / 2, (maxY + minY) / 2),
        radius = ((maxX - minX) + (maxY - minY)) / 4
    )
}
