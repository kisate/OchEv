package com.example.ochev.baseclasses

import com.example.ochev.baseclasses.dataclasses.InformationForNormalizer
import com.example.ochev.baseclasses.edgefigures.Edge
import com.example.ochev.baseclasses.edgefigures.normalizers.EdgeFigureNormalizer
import com.example.ochev.baseclasses.vertexfigures.VertexFigure
import com.example.ochev.baseclasses.vertexfigures.normalizers.NormalizerByML

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