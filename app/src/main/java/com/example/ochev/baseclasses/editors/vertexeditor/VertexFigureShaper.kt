package com.example.ochev.baseclasses.editors.vertexeditor

import com.example.ochev.baseclasses.dataclasses.Point


class VertexFigureShaper(val editor: VertexFigureEditor) {
    lateinit var currentMover: PointMover

    fun shapingBegins(point: Point): Boolean {
        val movers = editor.currentFigureState.getPointMovers()
        val bestMover = movers.minByOrNull { it.point.getDistanceToPoint(point) }!!

        val distance = bestMover.point.getDistanceToPoint(point)
        val newDistance = if (editor.currentFigureState.checkIfPointIsInside(point)) distance * 3 else distance
        return if (newDistance <= editor.currentFigureState.getDistanceToCountTouch()) {
            editor.graphEditor.history.saveState()
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