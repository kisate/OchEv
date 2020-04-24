package com.example.ochev.baseclasses

import com.example.ochev.baseclasses.dataclasses.Graph
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.edgefigures.EdgeFigureNormalizer
import com.example.ochev.baseclasses.vertexfigures.VertexFigureNormalizer

class FigureNormalizer {
    fun normaliseStrokes(strokes: MutableList<Stroke>, graph: Graph): Figure? {
        val asFigure = normalizeStrokesAsVertexFigure(strokes)
        if (asFigure != null) return asFigure

        val asEdge = normalizeStrokesAsEdgeFigure(strokes, graph)
        if (asEdge != null) return asEdge

        return null
    }

    fun normalizeStrokesAsVertexFigure(strokes: MutableList<Stroke>): VertexFigure? {
        val vertexFigureNormalizer = VertexFigureNormalizer()
        return vertexFigureNormalizer.normalizeByPatterns(strokes)
    }

    fun normalizeStrokesAsEdgeFigure(strokes: MutableList<Stroke>, graph: Graph): EdgeFigure? {
        val edgeFigureNormalizer = EdgeFigureNormalizer()
        return edgeFigureNormalizer.normalizeAsTwoClosestFigures(strokes, graph)
    }


}