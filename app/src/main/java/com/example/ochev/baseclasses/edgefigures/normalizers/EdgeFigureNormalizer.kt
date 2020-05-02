package com.example.ochev.baseclasses.edgefigures.normalizers

import com.example.ochev.baseclasses.EdgeFigure
import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.InfrormationForNormalizer
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.edgefigures.Line

class EdgeFigureNormalizer {

    private val MAX_DISTANCE_BETWEEN_END_AND_FIGURE = 50

    fun checkIfTheClosestigureIsCorrect(vertexFigure: VertexFigure, point: Point): Boolean {
        return vertexFigure.getDistanceToPointOrZeroIfInside(point) <=
                MAX_DISTANCE_BETWEEN_END_AND_FIGURE
    }

    fun normalizeAsTwoClosestFigures(information: InfrormationForNormalizer): EdgeFigure? {
        if (
            information.strokes == null ||
            information.graph == null
        ) return null

        val strokes = information.strokes
        val graph = information.graph

        val beginPoint = strokes[0].points[0]
        val endPoint = strokes.last().points.last()

        val beginFigure = graph.getClosestToPointVertexFigureOrNull(beginPoint)
        if (beginFigure == null ||
            !checkIfTheClosestigureIsCorrect(beginFigure, beginPoint)
        ) return null

        val endFigure = graph.getClosestToPointVertexFigureOrNull(endPoint)
        if (endFigure == null ||
            !checkIfTheClosestigureIsCorrect(endFigure, endPoint)
        ) return null

        if (beginFigure == endFigure) return null

        return Line(beginFigure, endFigure)
    }
}