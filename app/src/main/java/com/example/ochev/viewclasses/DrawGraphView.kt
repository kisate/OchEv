package com.example.ochev.viewclasses

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.ochev.baseclasses.EdgeFigure
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.Graph
import com.example.ochev.baseclasses.edgefigures.Line
import com.example.ochev.baseclasses.vertexfigures.Circle
import com.example.ochev.baseclasses.vertexfigures.Rectangle
import com.example.ochev.baseclasses.vertexfigures.Triangle

class DrawGraphView(
    context: Context?,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    val graph = DrawingGraph()
    val drawGraphInteractor = DrawGraphInteractor()

    fun clear() {
        graph.vertexes.clear()
        graph.edges.clear()
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        for (edge in graph.edges) {
            drawGraphInteractor.draw(edge, canvas)
        }
        for (vertex in graph.vertexes) {
            drawGraphInteractor.draw(vertex, canvas)
        }
    }
}

abstract class Drawer {
    protected val fontPaint = Paint()
    protected val circuitPaint = Paint()
    protected val fillPaint = Paint()
}

class CircleDrawer : Drawer() {

    init {
        fillPaint.style = Paint.Style.FILL
        fillPaint.strokeWidth = 0f
        fillPaint.color = Color.WHITE

        circuitPaint.style = Paint.Style.STROKE
        circuitPaint.strokeWidth = 10f
        circuitPaint.color = Color.BLACK
    }

    fun draw(vertex: Circle, drawingMode: DrawingMode, canvas: Canvas?) {
        canvas?.drawCircle(
            vertex.center.x.toFloat(),
            vertex.center.y.toFloat(),
            vertex.radius.toFloat(),
            fillPaint
        )
        canvas?.drawCircle(
            vertex.center.x.toFloat(),
            vertex.center.y.toFloat(),
            vertex.radius.toFloat(),
            circuitPaint
        )
    }
}

class RectangleDrawer : Drawer() {

    init {
        fillPaint.style = Paint.Style.FILL
        fillPaint.strokeWidth = 0f
        fillPaint.color = Color.WHITE

        circuitPaint.style = Paint.Style.STROKE
        circuitPaint.strokeWidth = 10f
        circuitPaint.color = Color.BLACK
    }

    fun draw(vertex: Rectangle, drawingMode: DrawingMode, canvas: Canvas?) {
        canvas?.drawRect(
            vertex.leftUpCorner.x.toFloat(),
            vertex.leftDownCorner.y.toFloat(),
            vertex.rightDownCorner.x.toFloat(),
            vertex.leftUpCorner.y.toFloat(),
            fillPaint
        )
        canvas?.drawRect(
            vertex.leftUpCorner.x.toFloat(),
            vertex.leftDownCorner.y.toFloat(),
            vertex.rightDownCorner.x.toFloat(),
            vertex.leftUpCorner.y.toFloat(),
            circuitPaint
        )
    }
}

class TriangleDrawer : Drawer() {

    init {
        fillPaint.style = Paint.Style.FILL
        fillPaint.strokeWidth = 0f
        fillPaint.color = Color.WHITE

        circuitPaint.style = Paint.Style.STROKE
        circuitPaint.strokeWidth = 10f
        circuitPaint.color = Color.BLACK
    }


    fun draw(vertex: Triangle, drawingMode: DrawingMode, canvas: Canvas?) {
        val path = Path()
        path.moveTo(vertex.pointA.x.toFloat(), vertex.pointA.y.toFloat())
        path.lineTo(vertex.pointB.x.toFloat(), vertex.pointB.y.toFloat())
        path.lineTo(vertex.pointC.x.toFloat(), vertex.pointC.y.toFloat())
        path.close()
        canvas?.drawPath(path, fillPaint)
        canvas?.drawPath(path, circuitPaint)
    }
}

class LineDrawer {
    private val paint = Paint()

    init {
        paint.style = Paint.Style.STROKE
        paint.color = Color.DKGRAY
        paint.strokeWidth = 10f
    }

    fun draw(edge: Line, drawingMode: DrawingMode, canvas: Canvas?) {
        val path = Path()
        val from = edge.beginFigure.center
        val to = edge.endFigure.center
        path.moveTo(from.x.toFloat(), from.y.toFloat())
        path.lineTo(to.x.toFloat(), to.y.toFloat())
        canvas?.drawPath(path, paint)
    }
}

class DrawGraphInteractor {
    private val circleDrawer = CircleDrawer()
    private val rectangleDrawer = RectangleDrawer()
    private val triangleDrawer = TriangleDrawer()
    private val lineDrawer = LineDrawer()

    fun draw(vertex: DrawingVertexFigure, canvas: Canvas?) {
        when (vertex.vertexFigure) {
            is Circle -> circleDrawer.draw(vertex.vertexFigure, vertex.drawingMode, canvas)
            is Rectangle -> rectangleDrawer.draw(vertex.vertexFigure, vertex.drawingMode, canvas)
            is Triangle -> triangleDrawer.draw(vertex.vertexFigure, vertex.drawingMode, canvas)
        }
    }

    fun draw(edge: DrawingEdgeFigure, canvas: Canvas?) {
        when (edge.edgeFigure) {
            is Line -> lineDrawer.draw(edge.edgeFigure, edge.drawingMode, canvas)
        }
    }


}

