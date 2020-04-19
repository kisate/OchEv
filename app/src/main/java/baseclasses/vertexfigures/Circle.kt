package baseclasses.vertexfigures

import baseclasses.FigureInteractor
import baseclasses.VertexFigure
import baseclasses.VertexFigureNormalizer
import baseclasses.dataclasses.Point
import baseclasses.dataclasses.Stroke
import baseclasses.dataclasses.StrokeInteractor
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

fun VertexFigureNormalizer.normalizeCircle(strokes: MutableList<Stroke>): Circle {
    val strokeInteractor = StrokeInteractor()
    val (maxX, maxY, minX, minY) = strokeInteractor.getStrokesRestrictions(strokes)
    return Circle(
        center = Point((maxX + minX) / 2, (maxY + minY) / 2),
        radius = ((maxX - minX) + (maxY - minY)) / 4
    )
}
