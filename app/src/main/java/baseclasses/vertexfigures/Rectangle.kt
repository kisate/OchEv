package baseclasses.vertexfigures

import baseclasses.VertexFigure
import baseclasses.VertexFigureNormalizer
import baseclasses.dataclasses.Point
import baseclasses.dataclasses.Stroke
import baseclasses.dataclasses.StrokeInteractor
import baseclasses.dataclasses.Vector


class Rectangle(
    figureText: MutableList<Char> = ArrayList(),
    texturePath: String = "",
    val leftDownCorner: Point = Point(),
    val rightUpCorner: Point = Point()
) : VertexFigure(figureText, texturePath) {
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
}


fun VertexFigureNormalizer.normalizeRectangle(strokes: MutableList<Stroke>): Rectangle {
    val strokeInteractor = StrokeInteractor()
    val (maxX, maxY, minX, minY) = strokeInteractor.getStrokesRestrictions(strokes)
    return Rectangle(
        leftDownCorner = Point(minX, minY),
        rightUpCorner = Point(maxX, maxY)
    )
}

