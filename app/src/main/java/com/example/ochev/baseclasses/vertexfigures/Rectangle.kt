package com.example.ochev.baseclasses.vertexfigures

import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.StrokeInteractor
import com.example.ochev.baseclasses.dataclasses.Vector
import com.example.ochev.baseclasses.vertexfigures.editors.PointMover
import kotlin.math.min


class Rectangle(
    val leftDownCorner: Point = Point(),
    val rightUpCorner: Point = Point()
) : VertexFigure() {
    val leftUpCorner: Point
        get() = Point(leftDownCorner.x, rightUpCorner.y)
    val rightDownCorner: Point
        get() = Point(rightUpCorner.x, leftDownCorner.y)

    override val center
        get() =
            Point(
                x = (leftDownCorner.x + rightUpCorner.x) / 2,
                y = (leftDownCorner.y + rightUpCorner.y) / 2
            )

    override fun moveByVector(vector: Vector) {
        leftDownCorner.moveByVector(vector)
        rightUpCorner.moveByVector(vector)
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
        val maxX = kotlin.math.max(leftDownCorner.x, rightDownCorner.x)
        val minX = min(leftDownCorner.x, rightDownCorner.x)
        val maxY = kotlin.math.max(leftDownCorner.y, leftUpCorner.y)
        val minY = min(leftDownCorner.y, leftUpCorner.y)

        return (point.x in minX..maxX && point.y in minY..maxY)
    }

    override fun getPointMovers(): MutableList<PointMover> {
        val points = mutableListOf(leftDownCorner, leftUpCorner, rightUpCorner, rightDownCorner)

        val result: MutableList<PointMover> = ArrayList()

        for (i in 0..3) {
            val moveFun =
                if (i % 2 == 0) {
                    { point: Point ->
                        points[i].x = point.x;points[i].y = point.y
                        points[(i - 1 + 4) % 4].y = point.y
                        points[(i + 1) % 4].x = point.x
                    }
                } else {
                    { point: Point ->
                        points[i].x = point.x;points[i].y = point.y
                        points[(i - 1 + 4) % 4].x = point.x
                        points[(i + 1) % 4].y = point.y
                    }
                }
            result.add(PointMover(points[i], moveFun))
        }
        return result
    }


}


fun VertexFigureBuilder.buildRectangle(strokes: MutableList<Stroke>): Rectangle {
    val strokeInteractor = StrokeInteractor()
    val (maxX, maxY, minX, minY) = strokeInteractor.getStrokesRestrictions(strokes)
    return Rectangle(
        leftDownCorner = Point(minX, minY),
        rightUpCorner = Point(maxX, maxY)
    )
}

