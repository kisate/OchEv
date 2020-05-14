package com.example.ochev.baseclasses.vertexfigures.normalizers

import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.InformationForNormalizer
import com.example.ochev.baseclasses.dataclasses.Stroke.Companion.getStrokesRestrictions
import com.example.ochev.baseclasses.dataclasses.Stroke.Companion.joinListOfStrokes
import com.example.ochev.baseclasses.vertexfigures.VertexFigureBuilder

class NormalizerByML {
    fun normalizeByML(information: InformationForNormalizer): VertexFigure? {
        if (
            information.classifier == null ||
            information.bitmap == null ||
            information.strokes == null
        ) return null

        val classifier = information.classifier
        val stroke = joinListOfStrokes(information.strokes)

        val (maxX, maxY, minX, minY) = getStrokesRestrictions(mutableListOf(stroke))

        if (stroke.points.first().getDistanceToPoint(stroke.points.last()) >=
            (maxX - minX + maxY - minY) / 4
        ) return null

        if (classifier.isInitialized) {
            val type = classifier.classify(information.bitmap, stroke)
            val vertexFigureBuilder = VertexFigureBuilder()

            return if (type == null) null
            else vertexFigureBuilder.buildFigure(information.strokes, type)
        }

        return null
    }

}