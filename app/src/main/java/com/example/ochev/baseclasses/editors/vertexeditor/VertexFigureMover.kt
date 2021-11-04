package com.example.ochev.baseclasses.editors.vertexeditor

import com.example.ochev.baseclasses.dataclasses.LineSegment
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Vector


class VertexFigureMover(val editor: VertexFigureEditor) {
    val helper: MoverHelper = MoverHelper(editor)

    lateinit var lastPoint: Point

    fun moveBegins(point: Point): Boolean {
        return if (editor.currentFigureState.checkIfFigureIsCloseEnough(point)) {
            editor.graphEditor.history.saveState()
            lastPoint = point
            true
        } else {
            false
        }
    }

    fun nextPoint(point: Point) {
        val move = Vector(lastPoint, point)
        lastPoint = point

        editor.changeFigure(editor.currentFigureState.movedByVector(move))
    }

    fun moveEnds(): List<LineSegment> {
        return listOfNotNull(helper.correct())
    }

}