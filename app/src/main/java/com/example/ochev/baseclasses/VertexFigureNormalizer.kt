package com.example.ochev.baseclasses

import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.vertexfigures.Vertexes
import com.example.ochev.baseclasses.vertexfigures.normalizeCircle
import com.example.ochev.baseclasses.vertexfigures.normalizeRectangle
import com.example.ochev.baseclasses.vertexfigures.normalizeTriangle

class VertexFigureNormalizer {
    fun normalizeFigure(strokes: MutableList<Stroke>, type: Vertexes): VertexFigure? {
        return when (type) {
            Vertexes.RECTANGLE -> normalizeRectangle(strokes)
            Vertexes.CIRCLE -> normalizeCircle(strokes)
            Vertexes.TRIANGLE -> normalizeTriangle(strokes)
            Vertexes.NOT_RECOGNIZED -> null
        }
    }
}

