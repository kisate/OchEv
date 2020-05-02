package com.example.ochev.baseclasses.vertexfigures.normalizers

import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.InformationForNormalizer
import com.example.ochev.baseclasses.dataclasses.PointInteractor
import com.example.ochev.baseclasses.dataclasses.StrokeInteractor
import com.example.ochev.baseclasses.vertexfigures.VertexFigureBuilder

class NormalizerByML {
    fun normalizeByML(information: InformationForNormalizer): VertexFigure? {
        if (
            information.classifier == null ||
            information.bitmap == null ||
            information.strokes == null
        ) return null

        val strokeInteractor = StrokeInteractor()
        val pointInteractor = PointInteractor()
        val classifier = information.classifier
        val stroke = strokeInteractor.joinListOfStrokes(information.strokes)

        val (maxX, maxY, minX, minY) = strokeInteractor.getStrokesRestrictions(mutableListOf(stroke))

        if (pointInteractor.distance(stroke.points.first(), stroke.points.last()) >=
            (maxX - minX + maxY - minY) / 4
        ) return null

        if (classifier.isInitialized) {
            val type = classifier.classify(information.bitmap, stroke)
            val vertexFigureBuilder = VertexFigureBuilder()

            return if (type == null) null
            else vertexFigureBuilder.buildFigure(mutableListOf(stroke), type)
        }

        return null
    }

}