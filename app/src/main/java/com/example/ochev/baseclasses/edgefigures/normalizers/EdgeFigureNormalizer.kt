package com.example.ochev.baseclasses.edgefigures.normalizers

import com.example.ochev.baseclasses.EdgeFigure
import com.example.ochev.baseclasses.dataclasses.InformationForNormalizer
import com.example.ochev.baseclasses.edgefigures.Line

class EdgeFigureNormalizer {


    fun normalizeAsTwoClosestFigures(information: InformationForNormalizer): EdgeFigure? {
        if (
            information.strokes == null ||
            information.graph == null
        ) return null

        val strokes = information.strokes
        val graph = information.graph

        val beginPoint = strokes[0].points[0]
        val endPoint = strokes.last().points.last()

        val beginFigure = graph.getClosestToPointVertexFigureOrNull(beginPoint)
        if (
            beginFigure == null ||
            !beginFigure.checkIfFigureIsCloseEnough(beginPoint)
        ) return null

        val endFigure = graph.getClosestToPointVertexFigureOrNull(endPoint)
        if (
            endFigure == null ||
            !endFigure.checkIfFigureIsCloseEnough(endPoint)
        ) return null

        if (beginFigure == endFigure) return null
        for (edge in graph.figures.edges) {
            val check = edge.first
            if (check.beginFigure == beginFigure && check.endFigure == endFigure) return null
        }

        return Line(beginFigure, endFigure)
    }
}