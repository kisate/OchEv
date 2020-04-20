package baseclasses.vertexfigures

import baseclasses.FigureInteractor
import baseclasses.VertexFigure
import baseclasses.VertexFigureNormalizer
import baseclasses.dataclasses.Point
import baseclasses.dataclasses.Stroke
import baseclasses.dataclasses.StrokeInteractor
import baseclasses.dataclasses.Vector

class Rectangle(
    figureText: MutableList<Char> = ArrayList(),
    texturePath: String = "",
    var leftDownCorner: Point = Point(),
    var rightUpCorner: Point = Point()
) : VertexFigure(figureText, texturePath)

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

fun VertexFigureNormalizer.normalizeRectangle(strokes: MutableList<Stroke>): Rectangle {
    val strokeInteractor = StrokeInteractor()
    val (maxX, maxY, minX, minY) = strokeInteractor.getStrokesRestrictions(strokes)
    return Rectangle(
        leftDownCorner = Point(minX, minY),
        rightUpCorner = Point(maxX, maxY)
    )
}

