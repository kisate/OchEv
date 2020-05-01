package com.example.ochev.baseclasses.vertexfigures

import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.StrokeInteractor
import com.example.ochev.baseclasses.dataclasses.Vector


class Rectangle(
    val leftDownCorner: Point = Point(),
    val rightUpCorner: Point = Point()
) : VertexFigure() {
    val leftUpCorner: Point
        get() = Point(leftDownCorner.x, rightUpCorner.y)
    val rightDownCorner: Point
        get() = Point(leftDownCorner.y, rightUpCorner.x)

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
        val triangleABC = Triangle(leftDownCorner, leftUpCorner, rightUpCorner)
        val triangleABD = Triangle(leftDownCorner, rightUpCorner, rightDownCorner)

        return (triangleABC.checkIfPointIsInside(point) || triangleABD.checkIfPointIsInside(point))
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

