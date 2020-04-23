package com.example.ochev.viewclasses

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.Graph
import com.example.ochev.baseclasses.edgefigures.Line
import com.example.ochev.baseclasses.vertexfigures.Circle

class DrawGraphView (
    context: Context?,
    attrs: AttributeSet? = null

) : View(context, attrs) {
    val graph = Graph()
    val drawGraphInteractor = DrawGraphInteractor()

    override fun onDraw(canvas: Canvas?) {
        for (vertex in graph.vertexes) {
            drawGraphInteractor.draw(vertex as Circle, canvas)
        }
    }
}

class DrawGraphInteractor {
    fun add(drawGraphView: DrawGraphView, figure: Figure) {
        when (figure) {
            is Circle -> addCircle(drawGraphView, figure)
        }
    }

    private fun addCircle(drawGraphView: DrawGraphView, figure: Circle) {
        drawGraphView.graph.addVertex(figure)
        drawGraphView.invalidate()
    }

    fun draw(vertex: Circle, canvas: Canvas?) {
        if (canvas != null) {
            canvas.drawCircle(vertex.center.x.toFloat(), vertex.center.y.toFloat(), vertex.radius.toFloat(), Paint())
        }
    }
}

