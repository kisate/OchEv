package com.example.ochev.baseclasses.vertexfigures.normalizers

import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.vertexfigures.VertexFigure
import com.example.ochev.baseclasses.vertexfigures.VertexFigureBuilder
import com.example.ochev.baseclasses.vertexfigures.Vertexes

class NormalizerByPatterns {
    fun getPenalty(strokes: MutableList<Stroke>, vertexFigure: VertexFigure): Float {
        return strokes.sumByDouble { stroke ->
            stroke.points.sumByDouble { point ->
                vertexFigure.getDistanceToPoint(point).toDouble()
            }
        }.toFloat() / strokes.sumBy { it.points.size }
    }

    fun getMostLikeFigure(strokes: MutableList<Stroke>): VertexFigure {
        val vertexFigureNormalizer =
            VertexFigureBuilder()
        val results: MutableList<VertexFigure> = ArrayList()
        for (type in Vertexes.values()) {
            results.add(vertexFigureNormalizer.buildFigure(strokes, type))
        }

        return results.minBy { vertexFigure ->
            getPenalty(strokes, vertexFigure)
        }!!
    }

    fun normalizeByPatterns(strokes: MutableList<Stroke>): VertexFigure {
        return getMostLikeFigure(strokes)
    }
}