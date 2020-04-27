package com.example.ochev.baseclasses.vertexfigures

import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.InfrormationForNormalizer
import com.example.ochev.baseclasses.dataclasses.StrokeInteractor
import com.example.ochev.ml.Utils

class NormalizerByML {
    fun normalizeByML(information: InfrormationForNormalizer): VertexFigure? {

        val strokeInteractor = StrokeInteractor()

        val bitmap = Utils.loadBitmapFromView(information.view!!)
        val classifier = information.classifier!!
        val stroke = strokeInteractor.joinListOfStrokes(information.strokes!!)

        if (bitmap != null && classifier.isInitialized) {
            val type = classifier.classify(bitmap, stroke)
            val vertexFigureBuilder = VertexFigureBuilder()

            return if (type == null) null
            else vertexFigureBuilder.buildFigure(mutableListOf(stroke), type)
        }

        return null
    }

}