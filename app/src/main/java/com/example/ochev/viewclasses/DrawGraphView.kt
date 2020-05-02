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

    val graph = Graph()
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

class FigureStyle{
    val fontPaint = Paint()
    val circuitPaint = Paint()
    val fillPaint = Paint()
}

abstract class Drawer {
    var currentStyle = 0
    val styles: MutableList<FigureStyle> = ArrayList()

    init {
        styles.add(FigureStyle())
    }

    fun setFontWidth(width: Float){
        for (style in styles) {
            style.fontPaint.strokeWidth = width
        }
    }

    fun setCircuitWidth(width: Float){
        for (style in styles) {
            style.circuitPaint.strokeWidth = width
        }
    }

    fun setFillWidth(width: Float){
        for (style in styles) {
            style.fillPaint.strokeWidth = width
        }
    }
}

class CircleDrawer : Drawer() {

    init {
        /*
            first style of circles
         */
        styles[0].fillPaint.style = Paint.Style.FILL
        styles[0].fillPaint.strokeWidth = 0f
        styles[0].fillPaint.color = Color.WHITE
        styles[0].circuitPaint.style = Paint.Style.STROKE
        styles[0].circuitPaint.strokeWidth = 10f
        styles[0].circuitPaint.color = Color.BLACK
        /*

         */
    }

    fun draw(vertex: Circle, canvas: Canvas?) {
        canvas?.drawCircle(
            vertex.center.x.toFloat(),
            vertex.center.y.toFloat(),
            vertex.radius.toFloat(),
            styles[currentStyle].fillPaint
        )
        canvas?.drawCircle(
            vertex.center.x.toFloat(),
            vertex.center.y.toFloat(),
            vertex.radius.toFloat(),
            styles[currentStyle].circuitPaint
        )
    }
}

class RectangleDrawer : Drawer() {

    init {
        /*
            first style of rectangles
         */
        styles[0].fillPaint.style = Paint.Style.FILL
        styles[0].fillPaint.strokeWidth = 0f
        styles[0].fillPaint.color = Color.WHITE
        styles[0].circuitPaint.style = Paint.Style.STROKE
        styles[0].circuitPaint.strokeWidth = 10f
        styles[0].circuitPaint.color = Color.BLACK
        /*

         */
    }

    fun draw(vertex: Rectangle, canvas: Canvas?) {
        canvas?.drawRect(
            vertex.leftDownCorner.x.toFloat(),
            vertex.leftDownCorner.y.toFloat(),
            vertex.rightUpCorner.x.toFloat(),
            vertex.rightUpCorner.y.toFloat(),
            styles[currentStyle].fillPaint
        )
        canvas?.drawRect(
            vertex.leftDownCorner.x.toFloat(),
            vertex.leftDownCorner.y.toFloat(),
            vertex.rightUpCorner.x.toFloat(),
            vertex.rightUpCorner.y.toFloat(),
            styles[currentStyle].circuitPaint
        )
    }
}

class TriangleDrawer : Drawer() {

    init {
        /*
            first style of triangels
         */
        styles[0].fillPaint.style = Paint.Style.FILL
        styles[0].fillPaint.strokeWidth = 0f
        styles[0].fillPaint.color = Color.WHITE
        styles[0].circuitPaint.style = Paint.Style.STROKE
        styles[0].circuitPaint.strokeWidth = 10f
        styles[0].circuitPaint.color = Color.BLACK
        /*

         */
    }


    fun draw(vertex: Triangle, canvas: Canvas?) {
        val path = Path()
        path.moveTo(vertex.pointA.x.toFloat(), vertex.pointA.y.toFloat())
        path.lineTo(vertex.pointB.x.toFloat(), vertex.pointB.y.toFloat())
        path.lineTo(vertex.pointC.x.toFloat(), vertex.pointC.y.toFloat())
        path.close()
        canvas?.drawPath(path, styles[currentStyle].fillPaint)
        canvas?.drawPath(path, styles[currentStyle].circuitPaint)
    }
}

class LineDrawer : Drawer() {

    init {
        /*
            first style of lines
         */
        styles[0].circuitPaint.style = Paint.Style.STROKE
        styles[0].circuitPaint.color = Color.DKGRAY
        styles[0].circuitPaint.strokeWidth = 10f
        /*

         */
    }

    fun draw(edge: Line, canvas: Canvas?) {
        val path = Path()
        val from = edge.beginFigure.center
        val to = edge.endFigure.center
        path.moveTo(from.x.toFloat(), from.y.toFloat())
        path.lineTo(to.x.toFloat(), to.y.toFloat())
        canvas?.drawPath(path, styles[currentStyle].circuitPaint)
    }
}

class DrawGraphInteractor {
    val circleDrawer = CircleDrawer()
    val rectangleDrawer = RectangleDrawer()
    val triangleDrawer = TriangleDrawer()
    val lineDrawer = LineDrawer()



    fun draw(vertex: VertexFigure, canvas: Canvas?) {
        when (vertex) {
            is Circle -> circleDrawer.draw(vertex, canvas)
            is Rectangle -> rectangleDrawer.draw(vertex, canvas)
            is Triangle -> triangleDrawer.draw(vertex, canvas)
        }
    }

    fun draw(edge: EdgeFigure, canvas: Canvas?) {
        when (edge) {
            is Line -> lineDrawer.draw(edge, canvas)
        }
    }


}

