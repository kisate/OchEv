package baseclasses.vertexfigures

import baseclasses.FigureInteractor
import baseclasses.VertexFigure
import baseclasses.dataclasses.Point
import baseclasses.dataclasses.Stroke
import baseclasses.dataclasses.Vector

class Rectangle(
    text: MutableList<Char> = ArrayList(),
    texturePath: String = "",
    var leftUpCorner: Point = Point(),
    var height: Int = 0,
    var width: Int = 0
) : VertexFigure(text, texturePath)

fun FigureInteractor.changeSize(rectangle: Rectangle, a: Stroke, b: Stroke) {
    TODO()
}

fun FigureInteractor.moveByVector(rectangle: Rectangle, direction: Vector) {
    rectangle.leftUpCorner.x += direction.x
    rectangle.leftUpCorner.y += direction.y
}

