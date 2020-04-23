package com.example.ochev.viewclasses

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.example.ochev.baseclasses.EdgeFigure
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.Graph
import com.example.ochev.baseclasses.edgefigures.Line
import com.example.ochev.baseclasses.vertexfigures.Circle
import com.example.ochev.baseclasses.vertexfigures.Rectangle

class DrawGraphView(
    context: Context?,
    attrs: AttributeSet? = null

) : View(context, attrs) {
    val graph = Graph()
    private val drawGraphInteractor = DrawGraphInteractor()

    override fun onDraw(canvas: Canvas?) {
        for (vertex in graph.vertexes) {
            drawGraphInteractor.draw(vertex, canvas)
        }
        for (edge in graph.edges) {
            drawGraphInteractor.draw(edge, canvas)
        }
    }
}

class CircleDrawer {
    private val paint = Paint()

    fun draw(vertex: Circle, canvas: Canvas?) {
        canvas?.drawCircle(
            vertex.center.x.toFloat(),
            vertex.center.y.toFloat(),
            vertex.radius.toFloat(),
            paint
        )
    }
}

class RectangleDrawer {
    private val paint = Paint()

    fun draw(vertex: Rectangle, canvas: Canvas?) {
        canvas?.drawRect(vertex.toRect(), paint)
    }
}

class DrawGraphInteractor {
    private val circleDrawer = CircleDrawer()
    private val rectangleDrawer = RectangleDrawer()

    fun add(drawGraphView: DrawGraphView, figure: Figure) {
        when (figure) {
            is VertexFigure -> drawGraphView.graph.addVertex(figure)
            is EdgeFigure -> drawGraphView.graph.addEdge(figure)
        }
        drawGraphView.invalidate()
    }

    fun draw(vertex: VertexFigure, canvas: Canvas?) {
        when (vertex) {
            is Circle -> circleDrawer.draw(vertex, canvas)
            is Rectangle -> rectangleDrawer.draw(vertex, canvas)
        }
    }

    fun draw(vertex: EdgeFigure, canvas: Canvas?) {

    }


}

