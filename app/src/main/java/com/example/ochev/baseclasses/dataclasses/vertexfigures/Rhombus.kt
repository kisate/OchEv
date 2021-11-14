package com.example.ochev.baseclasses.dataclasses.vertexfigures

import com.example.ochev.baseclasses.dataclasses.*
import com.example.ochev.baseclasses.dataclasses.Stroke.Companion.getStrokesRestrictions
import com.example.ochev.baseclasses.editors.vertexeditor.PointMover
import java.lang.Float.min
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sign

data class Rhombus(
    val leftCorner: Point = Point(),
    val upCorner: Point = Point()
) : VertexFigure() {
    override val center: Point
        get() = Point(upCorner.x, leftCorner.y)

    val rightCorner: Point
        get() = Point(
            upCorner.x - (leftCorner.x - upCorner.x),
            leftCorner.y
        )

    val downCorner: Point
        get() = Point(
            upCorner.x,
            leftCorner.y - (upCorner.y - leftCorner.y)
        )

    override val importantPoints: MutableList<Point>
        get() {
            return mutableListOf(leftCorner, upCorner, rightCorner, downCorner)
        }

    override fun clone(): VertexFigure {
        return this.copy()
    }


    override fun getIntersectionWithLineSegment(segment: LineSegment): MutableList<Point> {
        val result: MutableList<Point> = ArrayList()
        val points = importantPoints
        for (i in points.indices) {
            Point.intersectTwoSegments(
                segment,
                LineSegment(
                    points[i],
                    points[(i + 1) % points.size]
                )
            )
                ?.let { result.add(it) }
        }
        return result
    }

    override fun rescaledByFactor(factor: Float): VertexFigure {
        return this.copy(
            leftCorner = center.movedByVector(
                Vector(center, leftCorner).multipliedByFloat(factor)
            ),
            upCorner = center.movedByVector(
                Vector(center, upCorner).multipliedByFloat(factor)
            )
        )
    }


    override fun movedByVector(vector: Vector): VertexFigure {
        return this.copy(
            leftCorner = leftCorner.movedByVector(vector),
            upCorner = upCorner.movedByVector(vector)
        )
    }

    override fun checkIfPointIsInside(point: Point): Boolean {
        val points = importantPoints

        val signes = MutableList(points.size) {
            val side = Vector(points[it], points[(it + 1) % points.size])
            val vectorToPoint = Vector(points[it], point)
            side.vectorProduct(vectorToPoint)
        }

        signes.forEach { if (abs(it.sign - signes.first().sign) >= 0.00001) return false }
        return true
    }

    override fun getPointMovers(): MutableList<PointMover> {
//        TODO("IMPROVE")
        val points = importantPoints
        val result: MutableList<PointMover> = ArrayList()

        for (i in 0 until 4) {
            if (i % 2 == 0) {
                val moveFun = { point: Point ->
                    this.copy(
                        leftCorner = Point(point.x, leftCorner.y)
                    )
                }
                result.add(PointMover(points[i], moveFun))
            } else {
                val moveFun = { point: Point ->
                    this.copy(
                        upCorner = Point(upCorner.x, point.y)
                    )
                }
                result.add(PointMover(points[i], moveFun))
            }
        }

        return result
    }

    override fun getDistanceToPoint(point: Point): Float {
        val points = importantPoints

        return MutableList(points.size) {
            point.getDistanceToLineSegment(LineSegment(points[it], points[(it + 1) % points.size]))
        }.minOrNull()!!
    }

    override fun getDistanceToCountTouch(): Float {
        return max(
            min(
                leftCorner.getDistanceToPoint(rightCorner),
                upCorner.getDistanceToPoint(downCorner)
            ) / 3.33f, 27f
        )
    }

    override fun getFigureId(): FIGURE_ID {
        return FIGURE_ID.RHOMBUS
    }
}

fun VertexFigureBuilder.buildRhombus(strokes: MutableList<Stroke>): Rhombus {
    val (maxX, maxY, minX, minY) = getStrokesRestrictions(strokes)
    return Rhombus(
        leftCorner = Point(minX, (maxY + minY) / 2),
        upCorner = Point((maxX + minX) / 2, maxY)
    )
}