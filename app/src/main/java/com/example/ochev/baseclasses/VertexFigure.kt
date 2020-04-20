package com.example.ochev.baseclasses

import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.Vector
import com.example.ochev.baseclasses.vertexfigures.normalizeCircle
import com.example.ochev.baseclasses.vertexfigures.normalizeRectangle
import com.example.ochev.baseclasses.vertexfigures.normalizeTriangle

/*
A figure, that represents an information block in our scheme
 */

abstract class VertexFigure(
    figureText: MutableList<Char> = ArrayList(),
    var texturePath: String = ""
) : Figure(figureText) {
    abstract val center: Point

    abstract fun moveByVector(vector: Vector)
    abstract fun getDistanceToPoint(point: Point): Float
}


class VertexFigureNormalizer {
    fun normalizeFigure(strokes: MutableList<Stroke>, tag: String): VertexFigure? {
        when (tag) {
            "Rectanlge" -> return normalizeRectangle(strokes)
            "Circle" -> return normalizeCircle(strokes)
            "Triangle" -> return normalizeTriangle(strokes)
        }
        return null
    }
}

