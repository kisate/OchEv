package com.example.ochev.baseclasses.normalizers

import com.example.ochev.baseclasses.dataclasses.InformationForNormalizer
import com.example.ochev.baseclasses.editors.edgefigures.Edge

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

        val beginFigure = graphEditor.graph.getClosestToPointVertexFigureOrNull(beginPoint)
        if (
            beginFigure == null ||
            !beginFigure.figure.checkIfFigureIsCloseEnough(beginPoint)
        ) return null

        val endFigure = graphEditor.graph.getClosestToPointVertexFigureOrNull(endPoint)
        if (
            endFigure == null ||
            !endFigure.figure.checkIfFigureIsCloseEnough(endPoint)
        ) return null

        if (beginFigure.id == endFigure.id) return null
        for (edge in graphEditor.allEdges) {
            if ((edge.figure.from.figure == beginFigure.figure &&
                        edge.figure.to.figure == endFigure.figure) ||
                (edge.figure.from.figure == endFigure.figure &&
                        edge.figure.to.figure == beginFigure.figure)
            ) return null
        }

        return Edge(
            beginFigure,
            endFigure,
        )
    }
}