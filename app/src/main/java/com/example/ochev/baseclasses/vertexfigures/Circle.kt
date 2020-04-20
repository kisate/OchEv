package com.example.ochev.baseclasses.vertexfigures

import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.VertexFigureNormalizer
import com.example.ochev.baseclasses.dataclasses.*
import kotlin.math.abs

class Circle(
    figureText: MutableList<Char> = ArrayList(),
    texturePath: String = "",
    override val center: Point = Point(),
    val radius: Int = 0
) : VertexFigure(figureText, texturePath) {
    override fun getDistanceToPoint(point: Point): Float {
        val pointInteractor = PointInteractor()
        return abs(
            pointInteractor.getDistanceBetweenTwoPoints(center, point)
                    - radius
        )
    }

    override fun moveByVector(vector: Vector) {
        center.x += vector.x
        center.y += vector.y
    }

    override fun checkIfPointIsInside(point: Point): Boolean {
        val pointInteractor = PointInteractor()
        return pointInteractor.getDistanceBetweenTwoPoints(point, center) <= radius
    }
}

fun VertexFigureNormalizer.normalizeCircle(strokes: MutableList<Stroke>): Circle {
    val strokeInteractor = StrokeInteractor()
    val (maxX, maxY, minX, minY) = strokeInteractor.getStrokesRestrictions(strokes)
    return Circle(
        center = Point((maxX + minX) / 2, (maxY + minY) / 2),
        radius = ((maxX - minX) + (maxY - minY)) / 4
    )
}
