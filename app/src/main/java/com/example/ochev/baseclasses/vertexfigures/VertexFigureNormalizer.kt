package com.example.ochev.baseclasses.vertexfigures

import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.StrokeInteractor

class VertexFigureNormalizer {
    fun getPenalty(strokes: MutableList<Stroke>, vertexFigure: VertexFigure): Float {
        return strokes.sumByDouble { stroke ->
            stroke.points.sumByDouble { point ->
                vertexFigure.getDistanceToPoint(point).toDouble()
            }
        }.toFloat() / strokes.sumBy { it.points.size }
    }

    fun getMostLikeFigure(strokes: MutableList<Stroke>): VertexFigure {
        val vertexFigureNormalizer = VertexFigureNormalizer()
        val results: MutableList<VertexFigure> = ArrayList()
        for (type in Vertexes.values()) {
            results.add(vertexFigureNormalizer.normalizeFigure(strokes, type))
        }

        // now lets get figure with the smallest average distance to the strokes

        return results.minBy { vertexFigure ->
            getPenalty(strokes, vertexFigure)
        }!!
    }

    fun normalizeByPatterns(strokes: MutableList<Stroke>): VertexFigure? {
        val bestFigure = getMostLikeFigure(strokes)

        val penalty = getPenalty(strokes, bestFigure)

        val strokeInteractor = StrokeInteractor()

        val (maxX, maxY, minX, minY) = strokeInteractor.getStrokesRestrictions(strokes)
        return if (penalty >= (maxX - minX + maxY - minY) / 20) null
        else bestFigure
    }


    fun normalizeFigure(strokes: MutableList<Stroke>, type: Vertexes): VertexFigure {
        return when (type) {
            Vertexes.RECTANGLE -> normalizeRectangle(strokes)
            Vertexes.CIRCLE -> normalizeCircle(strokes)
            Vertexes.TRIANGLE -> normalizeTriangle(strokes)
        }
    }
}

