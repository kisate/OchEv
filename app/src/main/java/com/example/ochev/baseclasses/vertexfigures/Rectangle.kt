package com.example.ochev.baseclasses.vertexfigures

import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.StrokeInteractor
import com.example.ochev.baseclasses.dataclasses.Vector
import com.example.ochev.baseclasses.vertexfigures.editors.PointMover
import kotlin.math.max
import kotlin.math.min


class Rectangle(
    val leftDownCorner: Point = Point(),
    val rightUpCorner: Point = Point(),
    val leftUpCorner: Point = Point(leftDownCorner.x, rightUpCorner.y),
    val rightDownCorner: Point = Point(rightUpCorner.x, leftDownCorner.y)
) : VertexFigure() {


    override val center
        get() =
            Point(
                x = (leftDownCorner.x + rightUpCorner.x) / 2,
                y = (leftDownCorner.y + rightUpCorner.y) / 2
            )

    override fun moveByVector(vector: Vector) {
        leftDownCorner.moveByVector(vector)
        leftUpCorner.moveByVector(vector)
        rightUpCorner.moveByVector(vector)
        rightDownCorner.moveByVector(vector)
    }

    override fun getDistanceToPoint(point: Point): Float {
        // if the rectangle has points ABCD, we check dist till line segment AB,BC,CD,AD and take min
        // A - leftDown, B - leftUp, C - rightUp, D - rightDown

        // leftDown -> leftUp
        val tillAB = point.getDistanceToLineSegment(leftDownCorner, leftUpCorner)
        // leftUp -> rightUp
        val tillBC = point.getDistanceToLineSegment(leftUpCorner, rightUpCorner)
        // rightUp -> rightDown
        val tillCD = point.getDistanceToLineSegment(rightUpCorner, rightDownCorner)
        // rightDown -> leftDown
        val tillAD = point.getDistanceToLineSegment(rightDownCorner, leftDownCorner)

        return listOf(tillAB, tillBC, tillCD, tillAD).min()!!
    }

    override fun checkIfPointIsInside(point: Point): Boolean {
        val maxX = max(leftDownCorner.x, rightDownCorner.x)
        val minX = min(leftDownCorner.x, rightDownCorner.x)
        val maxY = max(leftDownCorner.y, leftUpCorner.y)
        val minY = min(leftDownCorner.y, leftUpCorner.y)

        return (point.x in minX..maxX && point.y in minY..maxY)
    }

    override fun getPointMovers(): MutableList<PointMover> {
        val points = getMovingPoints()

        val result: MutableList<PointMover> = ArrayList()

        for (i in 0..3) {
            val moveFun =
                if (i % 2 == 0) {
                    { point: Point ->
                        points[i].x = point.x
                        points[i].y = point.y
                        points[(i - 1 + 4) % 4].y = point.y
                        points[(i + 1) % 4].x = point.x
                    }
                } else {
                    { point: Point ->
                        points[i].x = point.x
                        points[i].y = point.y
                        points[(i - 1 + 4) % 4].x = point.x
                        points[(i + 1) % 4].y = point.y
                    }
                }
            result.add(PointMover(points[i], moveFun))
        }
        return result
    }

    override fun getMovingPoints(): MutableList<Point> {
        return mutableListOf(leftDownCorner, leftUpCorner, rightUpCorner, rightDownCorner)
    }


    override fun getDistanceToCountTouch(): Float {
        val dX = kotlin.math.abs(leftDownCorner.x - rightDownCorner.x)
        val dY = kotlin.math.abs(leftDownCorner.y - leftUpCorner.y)

        return max(min(dX, dY) / 4f, 20f)
    }

    fun repairOrderOfVertexes() {
        val maxX = max(leftDownCorner.x, rightDownCorner.x)
        val minX = min(leftDownCorner.x, rightDownCorner.x)
        val maxY = max(leftDownCorner.y, leftUpCorner.y)
        val minY = min(leftDownCorner.y, leftUpCorner.y)

        leftUpCorner.x = minX
        leftUpCorner.y = maxY
        leftDownCorner.x = minX
        leftDownCorner.y = minY
        rightDownCorner.x = maxX
        rightDownCorner.y = minY
        rightUpCorner.x = maxX
        rightUpCorner.y = maxY
    }


}


fun VertexFigureBuilder.buildRectangle(strokes: MutableList<Stroke>): Rectangle {
    val strokeInteractor = StrokeInteractor()
    val (maxX, maxY, minX, minY) = strokeInteractor.getStrokesRestrictions(strokes)
    return Rectangle(
        leftDownCorner = Point(minX, minY),
        leftUpCorner = Point(minX, maxY),
        rightDownCorner = Point(maxX, minY),
        rightUpCorner = Point(maxX, maxY)

    )
}

