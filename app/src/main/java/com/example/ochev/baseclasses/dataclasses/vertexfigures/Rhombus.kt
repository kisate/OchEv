package com.example.ochev.baseclasses.dataclasses.vertexfigures

import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.Stroke.Companion.getStrokesRestrictions
import com.example.ochev.baseclasses.dataclasses.Vector
import com.example.ochev.baseclasses.editors.vertexeditor.PointMover

data class Rhombus(
    val leftCorner: Point = Point(),
    val upCorner: Point = Point()
) : VertexFigure() {
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

    override fun getIntersectionWithLineSegment(a: Point, b: Point): MutableList<Point> {
        val result: MutableList<Point> = ArrayList()
        val points = importantPoints
        for (i in points.indices) {
            Point.intersectTwoSegments(a, b, points[i], points[(i + 1) % points.size])
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

    override val center: Point
        get() = Point(upCorner.x, leftCorner.y)

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

        signes.forEach { if (it != signes.first()) return false }
        return true
    }

    override fun getPointMovers(): MutableList<PointMover> {
        val points = getMovingPoints()
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

    override fun getMovingPoints(): MutableList<Point> {
        return importantPoints
    }

    override fun getDistanceToPoint(point: Point): Float {
        val points = importantPoints

        return MutableList(points.size) {
            point.getDistanceToLineSegment(points[it], points[(it + 1) % points.size])
        }.min()!!
    }

    override fun getDistanceToCountTouch(): Float {
        return leftCorner.getDistanceToPoint(upCorner) / 4f
    }
}

fun VertexFigureBuilder.buildRhombus(strokes: MutableList<Stroke>): Rhombus {
    val (maxX, maxY, minX, minY) = getStrokesRestrictions(strokes)
    return Rhombus(
        leftCorner = Point(minX, (maxY + minY) / 2),
        upCorner = Point(maxY, (maxX + minX) / 2)
    )
}