package com.example.ochev.baseclasses.vertexfigures

import android.util.Log
import android.widget.Toast
import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.InfrormationForNormalizer
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.StrokeInteractor
import com.example.ochev.ml.Utils

class VertexFigureNormalizer {
    fun getPenalty(strokes: MutableList<Stroke>, vertexFigure: VertexFigure): Float {
        return strokes.sumByDouble { stroke ->
            stroke.points.sumByDouble { point ->
                vertexFigure.getDistanceToPoint(point).toDouble()
            }
        }.toFloat() / strokes.sumBy { it.points.size }
    }

    fun getMostLikeFigure(strokes: MutableList<Stroke>): VertexFigure {
        val vertexFigureNormalizer = VertexFigureNormalizer()
        val results: MutableList<VertexFigure> = ArrayList()
        for (type in Vertexes.values()) {
            results.add(vertexFigureNormalizer.normalizeFigure(strokes, type))
        }

        // now lets get figure with the smallest average distance to the strokes

        return results.minBy { vertexFigure ->
            getPenalty(strokes, vertexFigure)
        }!!
    }

    fun normalizeByPatterns(strokes: MutableList<Stroke>): VertexFigure? {
        return getMostLikeFigure(strokes)
    }

    fun normalizeByML(information: InfrormationForNormalizer): VertexFigure? {

        val strokeInteractor = StrokeInteractor()

        val bitmap = Utils.loadBitmapFromView(information.view!!)
        val classifier = information.classifier!!
        val stroke = strokeInteractor.joinListOfStrokes(information.strokes!!)

        var figure: VertexFigure? = null

        if (bitmap != null && classifier.isInitialized) {
            classifier
                .classifyAsync(bitmap, stroke)
                .addOnSuccessListener { result: Vertexes ->
                    figure = normalizeFigure(information.strokes, result)
                    Toast.makeText(
                        classifier.context,
                        result.toString(), Toast.LENGTH_LONG
                    ).show()
                }
                .addOnFailureListener { e -> Log.e("Classify", "Error classifying", e) }

        }

        return figure
    }


    fun normalizeFigure(strokes: MutableList<Stroke>, type: Vertexes): VertexFigure {
        return when (type) {
            Vertexes.RECTANGLE -> normalizeRectangle(strokes)
            Vertexes.CIRCLE -> normalizeCircle(strokes)
            Vertexes.TRIANGLE -> normalizeTriangle(strokes)
        }
    }
}

