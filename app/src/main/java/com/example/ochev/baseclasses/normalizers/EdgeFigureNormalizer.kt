package com.example.ochev.baseclasses.normalizers

import com.example.ochev.baseclasses.dataclasses.InformationForNormalizer
import com.example.ochev.baseclasses.dataclasses.edgefigures.Edge

class EdgeFigureNormalizer {


    fun normalizeAsTwoClosestFigures(information: InformationForNormalizer): Edge? {
        if (
            information.strokes == null ||
            information.graphEditor == null
        ) return null

        val strokes = information.strokes
        val graphEditor = information.graphEditor

        val beginPoint = strokes[0].points[0]
        val endPoint = strokes.last().points.last()

        val beginFigure = graphEditor.getClosestToPointVertexFigureOrNull(beginPoint)
        if (
            beginFigure == null ||
            !beginFigure.figure.checkIfFigureIsCloseEnough(beginPoint)
        ) return null

        val endFigure = graphEditor.getClosestToPointVertexFigureOrNull(endPoint)
        if (
            endFigure == null ||
            !endFigure.figure.checkIfFigureIsCloseEnough(endPoint)
        ) return null

        if (beginFigure == endFigure) return null
        for (edge in graphEditor.allEdges) {
            if ((edge.figure.beginFigure == beginFigure.figure && edge.figure.endFigure == endFigure.figure) ||
                (edge.figure.beginFigure == endFigure.figure && edge.figure.endFigure == beginFigure.figure)
            ) return null
        }

        return Edge(
            beginFigure.figure,
            endFigure.figure
        )
    }
}