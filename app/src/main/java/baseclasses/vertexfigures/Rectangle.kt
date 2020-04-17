package baseclasses.vertexfigures

import baseclasses.FigureInteractor
import baseclasses.VertexFigure
import baseclasses.VertexFigureNormalizer
import baseclasses.dataclasses.Point
import baseclasses.dataclasses.Stroke
import baseclasses.dataclasses.Vector

class Rectangle(
    text: MutableList<Char> = ArrayList(),
    texturePath: String = "",
    var leftDownCorner: Point = Point(),
    var height: Int = 0,
    var width: Int = 0
) : VertexFigure(text, texturePath)

fun FigureInteractor.changeSize(rectangle: Rectangle, a: Stroke, b: Stroke) {
    TODO()
}

fun FigureInteractor.moveByVector(rectangle: Rectangle, direction: Vector) {
    rectangle.leftDownCorner.x += direction.x
    rectangle.leftDownCorner.y += direction.y
}

fun VertexFigureNormalizer.normalizeRectangle(strokes: MutableList<Stroke>): Rectangle {
    val (maxX, maxY, minX, minY) = getStrokesRestrictions(strokes)
    return Rectangle(
        leftDownCorner = Point(minX, minY, 0),
        height = maxY - minY,
        width = maxX - minX
    )
}

