package com.example.ochev.baseclasses.vertexfigures

import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.InfrormationForNormalizer
import com.example.ochev.baseclasses.dataclasses.StrokeInteractor

class NormalizerByML {
    fun normalizeByML(information: InfrormationForNormalizer): VertexFigure? {

        val strokeInteractor = StrokeInteractor()
        val classifier = information.classifier!!
        val stroke = strokeInteractor.joinListOfStrokes(information.strokes!!)

        if (information.bitmap != null && classifier.isInitialized) {
            val type = classifier.classify(information.bitmap, stroke)
            val vertexFigureBuilder = VertexFigureBuilder()

            return if (type == null) null
            else vertexFigureBuilder.buildFigure(mutableListOf(stroke), type)
        }

        return null
    }

}