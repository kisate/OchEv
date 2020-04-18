package baseclasses.vertexfigures

import baseclasses.FigureInteractor
import baseclasses.VertexFigure
import baseclasses.VertexFigureNormalizer
import baseclasses.dataclasses.Point
import baseclasses.dataclasses.Stroke
import baseclasses.dataclasses.Vector

class Triangle(
    figureText: MutableList<Char> = ArrayList(),
    texturePath: String = "",
    var pointA: Point = Point(),
    var pointB: Point = Point(),
    var pointC: Point = Point()
) : VertexFigure(figureText, texturePath)


fun FigureInteractor.changeSize(triangle: Triangle) {
    TODO()
}

fun FigureInteractor.moveByVector(triangle: Triangle, direction: Vector) {
    triangle.pointA.x += direction.x
    triangle.pointA.y += direction.y
    triangle.pointB.x += direction.x
    triangle.pointB.y += direction.y
    triangle.pointC.x += direction.x
    triangle.pointC.y += direction.y
}

fun VertexFigureNormalizer.normalizeTriangle(strokes: MutableList<Stroke>): Triangle {

}