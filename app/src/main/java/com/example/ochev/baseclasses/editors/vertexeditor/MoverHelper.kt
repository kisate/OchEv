package com.example.ochev.baseclasses.editors.vertexeditor

import com.example.ochev.baseclasses.dataclasses.LineSegment
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Vector

class MoverHelper(
    val editor: VertexFigureEditor
) {
    fun correctingSegments(): List<LineSegment> {
        val result: MutableList<LineSegment> = ArrayList()
        editor.graphEditor.allVertexes.forEach {
            if (it.figure != editor.currentFigureState)
                result += it.figure.getLinesToHelpMoving()
        }
        result.removeIf {
            editor.currentFigureState.center.getDistanceToLineSegment(it) > 50f
        }
        for (i in result.indices) {
            for (j in i + 1 until result.size) {
                if (result[i].toVector().scalarProduct(result[j].toVector()) <= 1e-5) {
                    return listOf(result[i], result[j])
                }
            }
        }
        return listOfNotNull(result.minByOrNull {
            editor.currentFigureState.center.getDistanceToLineSegment(
                it
            )
        })
    }

    fun getCorrector(): Vector? {
        val bestLine = correctingSegments()
        var resVector: Vector? = null
        val cur = editor.currentFigureState.center
        if (bestLine.size == 1) {
            val segmentToLine = Point.getOptimalSegment(
                bestLine[0],
                cur
            )
            resVector = Vector(segmentToLine.A, segmentToLine.B)
        }
        if (bestLine.size == 2) {
            val pt: Point? = Point.intersectTwoSegments(bestLine[0], bestLine[1])
            resVector = Vector(pt!!.x - cur.x, pt.y - cur.y)
        }
        return resVector
    }
}