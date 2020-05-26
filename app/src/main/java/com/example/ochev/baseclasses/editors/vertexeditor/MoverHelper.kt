package com.example.ochev.baseclasses.editors.vertexeditor

import com.example.ochev.baseclasses.dataclasses.LineSegment
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Vector

class MoverHelper(
    val editor: VertexFigureEditor
) {
    fun getPossibleLines(): MutableList<LineSegment> {
        val result: MutableList<LineSegment> = ArrayList()
        editor.graphEditor.allVertexes.forEach {
            if (it.figure != editor.currentFigureState)
                result += it.figure.getLinesToHelpMoving()
        }
        result.removeIf {
            editor.currentFigureState.center.getDistanceToLineSegment(it) >
                    editor.currentFigureState.getDistanceToCountTouch()
        }
        return result.subList(0, 5)
    }

    fun tryToHelp() {
        val bestLine = getPossibleLines().minBy {
            editor.currentFigureState.center.getDistanceToLineSegment(it)
        }
        if (bestLine != null) {
            val segmentToLine = Point.getOptimalSegment(
                bestLine,
                editor.currentFigureState.center
            )
            val direction = Vector(segmentToLine.A, segmentToLine.B)
            editor.changeFigure(editor.currentFigureState.movedByVector(direction))
        }

    }
}