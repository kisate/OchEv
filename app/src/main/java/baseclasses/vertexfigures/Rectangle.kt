package baseclasses.vertexfigures

import baseclasses.FigureInteractor
import baseclasses.VertexFigure
import baseclasses.VertexFigureNormalizer
import baseclasses.dataclasses.*


class Rectangle(
    figureText: MutableList<Char> = ArrayList(),
    texturePath: String = "",
    var leftDownCorner: Point = Point(),
    var rightUpCorner: Point = Point()
) : VertexFigure(figureText, texturePath) {
    val leftUpCorner: Point
        get() = Point(leftDownCorner.x, rightUpCorner.y)
    val rightDownCorner: Point
        get() = Point(rightUpCorner.x, leftDownCorner.y)
}

fun FigureInteractor.changeSize(rectangle: Rectangle, a: Stroke, b: Stroke) {
    TODO()
}

fun FigureInteractor.moveByVector(rectangle: Rectangle, direction: Vector) {
    rectangle.leftDownCorner.x += direction.x
    rectangle.leftDownCorner.y += direction.y
    rectangle.rightUpCorner.x += direction.x
    rectangle.rightUpCorner.y = direction.y
}

fun FigureInteractor.getCenter(rectangle: Rectangle): Point {
    return Point(
        x = (rectangle.leftDownCorner.x + rectangle.rightUpCorner.x) / 2,
        y = (rectangle.leftDownCorner.y + rectangle.rightUpCorner.y) / 2
    )
}

fun FigureInteractor.getDistanceBetweenFigureAndPoint(rectangle: Rectangle, point: Point): Float {
    val pointInteractor = PointInteractor()

    // if the reactangle has points ABCD, we check dist till line segment AB,BC,CD,AD and take min
    // A - leftDown, B - leftUp, C - rightUp, D - rightDown


    // AB
    val tillFirstLine = pointInteractor.getDistanceBetweenPointAndLineSegment(
        point,
        rectangle.leftDownCorner,
        rectangle.leftUpCorner
    )

    // BC
    val tillSecondLine = pointInteractor.getDistanceBetweenPointAndLineSegment(
        point,
        rectangle.leftUpCorner,
        rectangle.rightUpCorner
    )

    // CD
    val tillThirdLine = pointInteractor.getDistanceBetweenPointAndLineSegment(
        point,
        rectangle.rightUpCorner,
        rectangle.rightDownCorner
    )

    // AD
    val tillFourthLine = pointInteractor.getDistanceBetweenPointAndLineSegment(
        point,
        rectangle.rightDownCorner,
        rectangle.leftDownCorner
    )

    return listOf(
        tillFirstLine,
        tillSecondLine,
        tillThirdLine,
        tillFourthLine
    ).min()!!
}

fun VertexFigureNormalizer.normalizeRectangle(strokes: MutableList<Stroke>): Rectangle {
    val strokeInteractor = StrokeInteractor()
    val (maxX, maxY, minX, minY) = strokeInteractor.getStrokesRestrictions(strokes)
    return Rectangle(
        leftDownCorner = Point(minX, minY),
        rightUpCorner = Point(maxX, maxY)
    )
}

