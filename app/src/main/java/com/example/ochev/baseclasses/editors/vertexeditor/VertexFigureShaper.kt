package com.example.ochev.baseclasses.editors.vertexeditor

import com.example.ochev.baseclasses.dataclasses.Point


class VertexFigureShaper(val editor: VertexFigureEditor) {
    lateinit var currentMover: PointMover

    fun shapingBegins(point: Point): Boolean {
        editor.updateFigure()
        val movers = editor.currentFigureState.getPointMovers()
        val bestMover = movers.minBy { it.point.getDistanceToPoint(point) }!!

        return if (bestMover.point.getDistanceToPoint(point) <= editor.currentFigureState.getDistanceToCountTouch()) {
            currentMover = bestMover
            true
        } else {
            false
        }
    }

    fun nextPoint(point: Point) {
        editor.changeFigure(currentMover.moveFun(point))
    }


}