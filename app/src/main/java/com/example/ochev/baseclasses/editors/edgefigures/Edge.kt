package com.example.ochev.baseclasses.editors.edgefigures

import android.util.Log
import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.LineSegment
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.editors.grapheditor.Graph


// Figure that connects information blocks

data class Edge(
    val beginId: Int,
    val endId: Int,
    val graph: Graph
) : Figure() {
    val beginFigureNode
        get() = graph.getVertexFigureNodeByIdOrNull(beginId)!!
    val endFigureNode
        get() = graph.getVertexFigureNodeByIdOrNull(endId)!!
    val realBeginPoint: Point?
        get() {
            beginFigureNode.figure.getIntersectionWithLineSegment(
                LineSegment(
                    beginFigureNode.figure.center,
                    endFigureNode.figure.center
                )
            ).let {
                Log.i("EdgeDebugBegin", it.toString())
                return if (it.size >= 1) it.first()
                else null
            }
        }

    val realEndPoint: Point?
        get() {
            endFigureNode.figure.getIntersectionWithLineSegment(
                LineSegment(beginFigureNode.figure.center, endFigureNode.figure.center)
            ).let {
                Log.i("EdgeDebugEnd", it.toString())
                return if (it.size >= 1) it.first()
                else null
            }
        }


    override val center: Point
        get() = Point(
            (beginFigureNode.figure.center.x + endFigureNode.figure.center.x) / 2,
            (beginFigureNode.figure.center.y + endFigureNode.figure.center.y) / 2
        )

    fun withNewGraph(graph: Graph): Edge {
        return this.copy(graph = graph)
    }

    override fun checkIfFigureIsCloseEnough(point: Point): Boolean {
        return getDistanceToPoint(point) <= getDistanceToCountTouch()
    }

    override fun getDistanceToPoint(point: Point): Float {
        return point.getDistanceToLineSegment(
            LineSegment(beginFigureNode.figure.center, endFigureNode.figure.center)
        )
    }

    override fun getDistanceToCountTouch(): Float {
        return 40f
    }

    // returns 0 if begin is closer, 1 otherwise, -1 if both points are far away

    fun getIndexOfClosestEnd(point: Point): Int {
        val checker = { it: Point ->
            it.getDistanceToPoint(point) <= this.getDistanceToCountTouch()
        }

        if (realBeginPoint == null || realEndPoint == null) return -1

        return if (realBeginPoint!!.getDistanceToPoint(point) <= realEndPoint!!.getDistanceToPoint(
                point
            )
        ) {
            if (checker(realBeginPoint!!)) 0
            else -1
        } else {
            if (checker(realEndPoint!!)) 1
            else -1
        }
    }

}