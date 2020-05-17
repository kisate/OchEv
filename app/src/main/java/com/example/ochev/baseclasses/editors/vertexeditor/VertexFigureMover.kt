package com.example.ochev.baseclasses.editors.vertexeditor

import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Vector


class VertexFigureMover(val editor: VertexFigureEditor) {
    lateinit var lastPoint: Point

    fun moveBegins(point: Point): Boolean {
        return if (editor.figureUnderControl.checkIfFigureIsCloseEnough(point)) {
            lastPoint = point
            true
        } else {
            false
        }
    }

    fun nextPoint(point: Point) {
        val move = Vector(lastPoint, point)
        lastPoint = point

        editor.changeFigure(editor.figureUnderControl.movedByVector(move))
    }

}