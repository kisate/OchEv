package com.example.ochev.baseclasses.dataclasses.edgefigures.normalizers

import com.example.ochev.baseclasses.dataclasses.InformationForNormalizer
import com.example.ochev.baseclasses.dataclasses.edgefigures.Edge

class EdgeFigureNormalizer {


    fun normalizeAsTwoClosestFigures(information: InformationForNormalizer): Edge? {
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
            !beginFigure.figure.checkIfFigureIsCloseEnough(beginPoint)
        ) return null

        val endFigure = graph.getClosestToPointVertexFigureOrNull(endPoint)
        if (
            endFigure == null ||
            !endFigure.figure.checkIfFigureIsCloseEnough(endPoint)
        ) return null

        if (beginFigure == endFigure) return null
        for (edge in graph.figures.edges) {
            if (edge.figure.beginFigure == beginFigure.figure &&
                edge.figure.endFigure == endFigure.figure
            ) return null
        }

        return Edge(
            beginFigure.figure,
            endFigure.figure
        )
    }
}