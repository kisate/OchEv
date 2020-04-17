package baseclasses.vertexfigures

import baseclasses.FigureInteractor
import baseclasses.VertexFigure
import baseclasses.dataclasses.Point
import baseclasses.dataclasses.Vector

class Circle(
    figureText: MutableList<Char> = ArrayList(),
    texturePath: String = "",
    var center: Point = Point(),
    var radius: Int = 0
) : VertexFigure(figureText, texturePath)


fun FigureInteractor.changeSize(circle: Circle, a: Point, b: Point) {
    TODO()
}

fun FigureInteractor.moveByVector(circle: Circle, direction: Vector) {
    circle.center.x += direction.x
    circle.center.y += direction.y
}

