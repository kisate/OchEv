package com.example.ochev.baseclasses

import com.example.ochev.baseclasses.dataclasses.InfrormationForNormalizer
import com.example.ochev.baseclasses.edgefigures.normalizers.EdgeFigureNormalizer
import com.example.ochev.baseclasses.vertexfigures.normalizers.NormalizerByML

class FigureNormalizer {
    fun normaliseStrokes(
        information: InfrormationForNormalizer
    ): Figure? {
        val asFigure = normalizeStrokesAsVertexFigure(information)
        if (asFigure != null) return asFigure

        val asEdge = normalizeStrokesAsEdgeFigure(information)
        if (asEdge != null) return asEdge

        return null
    }

    fun normalizeStrokesAsVertexFigure(
        information: InfrormationForNormalizer
    ): VertexFigure? {
        val mlNormalizer =
            NormalizerByML()
        return mlNormalizer.normalizeByML(information)
    }

    fun normalizeStrokesAsEdgeFigure(information: InfrormationForNormalizer): EdgeFigure? {
        val edgeFigureNormalizer =
            EdgeFigureNormalizer()
        return edgeFigureNormalizer.normalizeAsTwoClosestFigures(information)
    }


}