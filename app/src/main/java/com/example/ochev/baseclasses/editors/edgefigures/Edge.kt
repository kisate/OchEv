package com.example.ochev.baseclasses.editors.edgefigures

import android.util.Log
import com.example.ochev.baseclasses.dataclasses.FIGURE_ID
import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.LineSegment
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.nodes.VertexFigureNode
import com.example.ochev.baseclasses.editors.grapheditor.Graph


// Figure that connects information blocks

data class Edge(
    val from: VertexFigureNode,
    val to: VertexFigureNode,
) : Figure() {
    val realBeginPoint: Point?
        get() {
            from.figure.getIntersectionWithLineSegment(
                LineSegment(
                    from.figure.center,
                    to.figure.center
                )
            ).let {
                Log.i("EdgeDebugBegin", it.toString())
                return if (it.size >= 1) it.first()
                else null
            }
        }

    val realEndPoint: Point?
        get() {
            to.figure.getIntersectionWithLineSegment(
                LineSegment(from.figure.center, to.figure.center)
            ).let {
                Log.i("EdgeDebugEnd", it.toString())
                return if (it.size >= 1) it.first()
                else null
            }
        }


    override val center: Point
        get() = Point(
            (from.figure.center.x + to.figure.center.x) / 2,
            (from.figure.center.y + to.figure.center.y) / 2
        )

    fun withNewGraph(graph: Graph): Edge {
        return this.copy(from = graph.getVertexFigureNodeByIdOrNull(from.id)!!, to = graph.getVertexFigureNodeByIdOrNull(to.id)!!)
    }

    override fun checkIfFigureIsCloseEnough(point: Point): Boolean {
        return getDistanceToPoint(point) <= getDistanceToCountTouch()
    }

    override fun getFigureId(): FIGURE_ID {
        return FIGURE_ID.EDGE
    }

    override fun getDistanceToPoint(point: Point): Float {
        return point.getDistanceToLineSegment(
            LineSegment(from.figure.center, to.figure.center)
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