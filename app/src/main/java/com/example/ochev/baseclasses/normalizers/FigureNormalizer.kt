package com.example.ochev.baseclasses.normalizers

import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.InformationForNormalizer
import com.example.ochev.baseclasses.dataclasses.edgefigures.Edge
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure

class FigureNormalizer {

    fun normaliseStrokes(
        information: InformationForNormalizer
    ): Figure? {
        val asFigure = normalizeStrokesAsVertexFigure(information)
        if (asFigure != null) return asFigure

        val asEdge = normalizeStrokesAsEdgeFigure(information)
        if (asEdge != null) return asEdge

        return null
    }

    fun normalizeStrokesAsVertexFigure(
        information: InformationForNormalizer
    ): VertexFigure? {
        val mlNormalizer =
            NormalizerByML()
        return mlNormalizer.normalizeByML(information)
    }

    fun normalizeStrokesAsEdgeFigure(information: InformationForNormalizer): Edge? {
        val edgeFigureNormalizer =
            EdgeFigureNormalizer()
        return edgeFigureNormalizer.normalizeAsTwoClosestFigures(information)
    }


}