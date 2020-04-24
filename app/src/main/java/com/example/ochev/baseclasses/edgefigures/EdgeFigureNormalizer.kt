package com.example.ochev.baseclasses.edgefigures

import com.example.ochev.baseclasses.EdgeFigure
import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.Graph
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke

class EdgeFigureNormalizer {
    fun checkIfTheClosestigureIsCorrect(vertexFigure: VertexFigure, point: Point): Boolean {
        val distance =
            if (vertexFigure.checkIfPointIsInside(point)) 0f
            else vertexFigure.getDistanceToPoint(point)
        return distance <= 50
    }

    fun normalizeAsTwoClosestFigures(strokes: MutableList<Stroke>, graph: Graph): EdgeFigure? {
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