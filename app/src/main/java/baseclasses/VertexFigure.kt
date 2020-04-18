package baseclasses

import baseclasses.dataclasses.Stroke
import baseclasses.vertexfigures.normalizeCircle
import baseclasses.vertexfigures.normalizeRectangle
import baseclasses.vertexfigures.normalizeTriangle

/*
A figure, that represents an information block in our scheme
 */

abstract class VertexFigure(
    figureText: MutableList<Char> = ArrayList(),
    var texturePath: String = ""
) : Figure(figureText)


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