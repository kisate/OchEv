package com.example.ochev.baseclasses.editors.vertexeditor

import com.example.ochev.baseclasses.dataclasses.Point


class VertexFigureShaper(val editor: VertexFigureEditor) {
    lateinit var currentMover: PointMover

    fun shapingBegins(point: Point): Boolean {
        val movers = editor.figureUnderControl.getPointMovers()
        val bestMover = movers.minBy { it.point.getDistanceToPoint(point) }!!

        return if (bestMover.point.getDistanceToPoint(point) <= editor.figureUnderControl.getDistanceToCountTouch()) {
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