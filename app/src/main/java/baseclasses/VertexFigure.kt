package baseclasses

import baseclasses.dataclasses.Point
import baseclasses.dataclasses.Stroke
import baseclasses.dataclasses.Vector
import baseclasses.vertexfigures.normalizeCircle
import baseclasses.vertexfigures.normalizeRectangle
import baseclasses.vertexfigures.normalizeTriangle

/*
A figure, that represents an information block in our scheme
 */

abstract class VertexFigure(
    figureText: MutableList<Char> = ArrayList(),
    var texturePath: String = ""
) : Figure(figureText) {
    abstract val center: Point

    abstract fun moveByVector(vector: Vector)
    abstract fun getDistanceToPoint(point: Point): Float
    abstract fun checkIfPointIsInside(point: Point): Boolean
}


class VertexFigureNormalizer {
    fun normalizeFigure(strokes: MutableList<Stroke>, tag: String): VertexFigure? {
        when (tag) {
            "Rectanlge" -> return normalizeRectangle(strokes)
            "Circle" -> return normalizeCircle(strokes)
            "Triangle" -> return normalizeTriangle(strokes)
        }
        return null
    }
}

