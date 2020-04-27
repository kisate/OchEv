package com.example.ochev.baseclasses.vertexfigures

import com.example.ochev.baseclasses.Constants
import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.InfrormationForNormalizer
import com.example.ochev.baseclasses.dataclasses.PointInteractor
import com.example.ochev.baseclasses.dataclasses.StrokeInteractor

class NormalizerByML {
    fun normalizeByML(information: InfrormationForNormalizer): VertexFigure? {
        if (
            information.classifier == null ||
            information.bitmap == null ||
            information.strokes == null
        ) return null

        val strokeInteractor = StrokeInteractor()
        val pointInteractor = PointInteractor()
        val classifier = information.classifier
        val stroke = strokeInteractor.joinListOfStrokes(information.strokes)

        if (pointInteractor.distance(stroke.points.first(), stroke.points.last()) >=
            Constants.MAX_DISTANCE_BETWEEN_STROKE_ENDS.value
        ) {
            return null
        }

        if (classifier.isInitialized) {
            val type = classifier.classify(information.bitmap, stroke)
            val vertexFigureBuilder = VertexFigureBuilder()

            return if (type == null) null
            else vertexFigureBuilder.buildFigure(mutableListOf(stroke), type)
        }

        return null
    }

}