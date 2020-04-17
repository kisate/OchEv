package baseclasses

import baseclasses.dataclasses.Stroke
import baseclasses.dataclasses.StrokeInteractor
import baseclasses.vertexfigures.normalizeCircle
import baseclasses.vertexfigures.normalizeRectangle

/*
A figure, that represents an information block in our scheme
 */

abstract class VertexFigure(
    figureText: MutableList<Char> = ArrayList(),
    var texturePath: String = ""
) : Figure(figureText)


class VertexFigureNormalizer {
    fun getStrokesRestrictions(strokes: MutableList<Stroke>): List<Int> {
        val strokeInteractor = StrokeInteractor()
        var maxX = 0
        var minX = Int.MAX_VALUE
        var maxY = 0
        var minY = Int.MAX_VALUE
        for (stroke in strokes) {
            maxX = Integer.max(maxX, strokeInteractor.maxX(stroke))
            maxY = Integer.max(maxY, strokeInteractor.maxY(stroke))
            minX = Math.min(minX, strokeInteractor.minX(stroke))
            minY = Math.min(minY, strokeInteractor.minY(stroke))
        }
        return listOf(maxX, maxY, minX, minY)
    }

    fun normalizeFigure(strokes: MutableList<Stroke>, tag: String): VertexFigure? {
        when (tag) {
            "Rectanlge" -> return normalizeRectangle(strokes)
            "Circle" -> return normalizeCircle(strokes)
        }
        return null
    }
}