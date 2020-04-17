package baseclasses.vertexfigures

import baseclasses.VertexFigure
import baseclasses.dataclasses.Point
import baseclasses.dataclasses.Vector
import baseclasses.interactors.FigureMover
import baseclasses.interactors.FigureResizer

class Circle(
    figureText: MutableList<Char> = ArrayList(),
    texturePath: String = "",
    var center: Point = Point(),
    var radius: Int = 0
) : VertexFigure(figureText, texturePath)


fun FigureResizer.resizeByTwoPoints(circle: Circle, a: Point, b: Point) {
    TODO()
}

fun FigureMover.moveByVector(circle: Circle, direction: Vector) {
    circle.center.x += direction.x
    circle.center.y += direction.y
}

