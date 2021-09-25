package com.example.ochev.baseclasses.normalizers

import android.graphics.Bitmap
import com.example.ochev.baseclasses.dataclasses.InformationForNormalizer
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.Stroke.Companion.getStrokesRestrictions
import com.example.ochev.baseclasses.dataclasses.Stroke.Companion.joinListOfStrokes
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigureBuilder

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
            val bitmap = prepareBitmap(information.bitmap, stroke)
            val type = classifier.classify(bitmap)
            val vertexFigureBuilder = VertexFigureBuilder()

            return if (type == null) null
            else vertexFigureBuilder.buildFigure(information.strokes, type)
        }

        return null
    }

    private fun prepareBitmap(bitmap: Bitmap, stroke: Stroke): Bitmap {
        val minX = stroke.minX()
        val minY = stroke.minY()
        val maxX = stroke.maxX()
        val maxY = stroke.maxY()

        return Bitmap.createBitmap(
            bitmap,
            minX.toInt(),
            minY.toInt(),
            (maxX - minX).toInt(),
            (maxY - minY).toInt()
        )
    }


}