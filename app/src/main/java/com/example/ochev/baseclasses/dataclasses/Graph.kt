package com.example.ochev.baseclasses.dataclasses

import com.example.ochev.baseclasses.EdgeFigure
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.FigureNormalizer
import com.example.ochev.baseclasses.VertexFigure


data class Graph(
    val figures: FigureContainer = FigureContainer()
) {

    fun getFigureForEditing(point: Point): Figure? {
        val bestFigure = figures.getClosestToPointFigureOrNull(point)
        return if (bestFigure == null || !bestFigure.checkIfFigureIsCloseEnough(point)) null
        else bestFigure
    }

    fun modifyByStrokes(information: InformationForNormalizer) {
        val normalizer = FigureNormalizer()

        val result = normalizer.normaliseStrokes(information) ?: return

        when (result) {
            is VertexFigure -> figures.addVertex(result, figures.maxHeight + 1)
            is EdgeFigure -> figures.addEdge(
                result,
                kotlin.math.min(
                    figures.getHeight(result.beginFigure),
                    figures.getHeight(result.endFigure)
                )
            )
        }
    }


}