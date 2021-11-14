package com.example.ochev.baseclasses.editors.vertexeditor

import com.example.ochev.baseclasses.dataclasses.LineSegment
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Vector

class MoverHelper(
    val editor: VertexFigureEditor
) {
    fun correctingSegments(): List<LineSegment> {
        val result: MutableList<LineSegment> = ArrayList()
        val curCenter = editor.currentFigureState.center
        editor.graphEditor.allVertexes.forEach {
            if (it.figure != editor.currentFigureState)
                result += it.figure.getLinesToHelpMoving()
        }
        result.removeIf {
            curCenter.getDistanceToLineSegment(it) > 50f
        }
        val intersectionResult = getCorrectingIntersection(result, curCenter)
        if (intersectionResult.isNotEmpty()) {
            return intersectionResult
        }
        return listOfNotNull(result.minByOrNull {
            curCenter.getDistanceToLineSegment(
                it
            )
        })
    }

    private fun getCorrectingIntersection(
        segments: List<LineSegment>,
        curCenter: Point
    ): List<LineSegment> {
        var lastDiff = Float.MAX_VALUE
        var ans = listOf<LineSegment>()
        for (i in segments.indices) {
            for (j in i + 1 until segments.size) {
                if (segments[i].toVector().scalarProduct(segments[j].toVector()) <= 1e-5) {
                    val pt: Point? = Point.intersectTwoSegments(segments[i], segments[j])
                    if (lastDiff > curCenter.getDistanceToPoint(pt!!)) {
                        lastDiff = curCenter.getDistanceToPoint(pt)
                        ans = listOf(segments[i], segments[j])
                    }
                }
            }
        }
        return ans
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