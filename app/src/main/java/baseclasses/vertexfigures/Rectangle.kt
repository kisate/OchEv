package baseclasses.vertexfigures

import baseclasses.VertexFigure
import baseclasses.dataclasses.Point
import baseclasses.dataclasses.Vector
import baseclasses.interactors.FigureMover
import baseclasses.interactors.FigureResizer

class Rectangle(
    text: MutableList<Char> = ArrayList(),
    texturePath: String = "",
    var leftUpCorner: Point = Point(),
    var height: Int = 0,
    var width: Int = 0
) : VertexFigure(text, texturePath)

fun FigureResizer.resizeByTwoPoints(rectangle: Rectangle, a: Point, b: Point) {
    TODO()
}

fun FigureMover.moveByVector(rectangle: Rectangle, direction: Vector) {
    rectangle.leftUpCorner.x += direction.x
    rectangle.leftUpCorner.y += direction.y
}

