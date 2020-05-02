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
        for (figure in graph.figuresSortedByHeights) {
            drawGraphInteractor.draw(figure, canvas)
        }
    }
}

class FigureStyle {
    val fontPaint = Paint()
    val circuitPaint = Paint()
    val fillPaint = Paint()
}

abstract class Drawer {
    var currentStyle = 0
    val styles: MutableList<FigureStyle> = ArrayList()

    init {
        styles.add(FigureStyle())
        styles.add(FigureStyle())
    }

    fun setFontWidth(width: Float) {
        for (style in styles) {
            style.fontPaint.strokeWidth = width
        }
    }

    fun setCircuitWidth(width: Float) {
        for (style in styles) {
            style.circuitPaint.strokeWidth = width
        }
    }

    fun setFillWidth(width: Float) {
        for (style in styles) {
            style.fillPaint.strokeWidth = width
        }
    }
}

class CircleDrawer : Drawer() {

    init {
        /*
            default style of circles
         */
        styles[DrawingMode.DEFAULT.ordinal].fillPaint.style = Paint.Style.FILL
        styles[DrawingMode.DEFAULT.ordinal].fillPaint.strokeWidth = 0f
        styles[DrawingMode.DEFAULT.ordinal].fillPaint.color = Color.WHITE
        styles[DrawingMode.DEFAULT.ordinal].circuitPaint.style = Paint.Style.STROKE
        styles[DrawingMode.DEFAULT.ordinal].circuitPaint.strokeWidth = 10f
        styles[DrawingMode.DEFAULT.ordinal].circuitPaint.color = Color.BLACK
        /*
            editing style of circles
         */
        styles[DrawingMode.EDIT.ordinal].fillPaint.style = Paint.Style.FILL
        styles[DrawingMode.EDIT.ordinal].fillPaint.strokeWidth = 0f
        styles[DrawingMode.EDIT.ordinal].fillPaint.color = Color.WHITE
        styles[DrawingMode.EDIT.ordinal].circuitPaint.style = Paint.Style.STROKE
        styles[DrawingMode.EDIT.ordinal].circuitPaint.strokeWidth = 10f
        styles[DrawingMode.EDIT.ordinal].circuitPaint.color = Color.BLUE
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
            default style of rectangles
         */
        styles[DrawingMode.DEFAULT.ordinal].fillPaint.style = Paint.Style.FILL
        styles[DrawingMode.DEFAULT.ordinal].fillPaint.strokeWidth = 0f
        styles[DrawingMode.DEFAULT.ordinal].fillPaint.color = Color.WHITE
        styles[DrawingMode.DEFAULT.ordinal].circuitPaint.style = Paint.Style.STROKE
        styles[DrawingMode.DEFAULT.ordinal].circuitPaint.strokeWidth = 10f
        styles[DrawingMode.DEFAULT.ordinal].circuitPaint.color = Color.BLACK
        /*
            editing style of rectangles
         */
        styles[DrawingMode.EDIT.ordinal].fillPaint.style = Paint.Style.FILL
        styles[DrawingMode.EDIT.ordinal].fillPaint.strokeWidth = 0f
        styles[DrawingMode.EDIT.ordinal].fillPaint.color = Color.WHITE
        styles[DrawingMode.EDIT.ordinal].circuitPaint.style = Paint.Style.STROKE
        styles[DrawingMode.EDIT.ordinal].circuitPaint.strokeWidth = 10f
        styles[DrawingMode.EDIT.ordinal].circuitPaint.color = Color.BLUE
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
            default style of triangels
         */
        styles[DrawingMode.DEFAULT.ordinal].fillPaint.style = Paint.Style.FILL
        styles[DrawingMode.DEFAULT.ordinal].fillPaint.strokeWidth = 0f
        styles[DrawingMode.DEFAULT.ordinal].fillPaint.color = Color.WHITE
        styles[DrawingMode.DEFAULT.ordinal].circuitPaint.style = Paint.Style.STROKE
        styles[DrawingMode.DEFAULT.ordinal].circuitPaint.strokeWidth = 10f
        styles[DrawingMode.DEFAULT.ordinal].circuitPaint.color = Color.BLACK
        /*
            editing style of triangles
         */
        styles[DrawingMode.EDIT.ordinal].fillPaint.style = Paint.Style.FILL
        styles[DrawingMode.EDIT.ordinal].fillPaint.strokeWidth = 0f
        styles[DrawingMode.EDIT.ordinal].fillPaint.color = Color.WHITE
        styles[DrawingMode.EDIT.ordinal].circuitPaint.style = Paint.Style.STROKE
        styles[DrawingMode.EDIT.ordinal].circuitPaint.strokeWidth = 10f
        styles[DrawingMode.EDIT.ordinal].circuitPaint.color = Color.BLUE
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
            default style of lines
         */
        styles[DrawingMode.DEFAULT.ordinal].circuitPaint.style = Paint.Style.STROKE
        styles[DrawingMode.DEFAULT.ordinal].circuitPaint.color = Color.DKGRAY
        styles[DrawingMode.DEFAULT.ordinal].circuitPaint.strokeWidth = 10f
        /*
            editing stype of lines
         */
        styles[DrawingMode.EDIT.ordinal].circuitPaint.style = Paint.Style.STROKE
        styles[DrawingMode.EDIT.ordinal].circuitPaint.color = Color.BLUE
        styles[DrawingMode.EDIT.ordinal].circuitPaint.strokeWidth = 10f
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

    fun draw(figure: Figure, canvas: Canvas?) {

        when (figure) {
            is Circle -> {
                circleDrawer.currentStyle = figure.drawingInformation.drawingMode.ordinal
                circleDrawer.draw(figure, canvas)
            }
            is Rectangle -> {
                rectangleDrawer.currentStyle = figure.drawingInformation.drawingMode.ordinal
                rectangleDrawer.draw(figure, canvas)
            }
            is Triangle -> {
                triangleDrawer.currentStyle = figure.drawingInformation.drawingMode.ordinal
                triangleDrawer.draw(figure, canvas)
            }
            is Line -> {
                lineDrawer.currentStyle = figure.drawingInformation.drawingMode.ordinal
                lineDrawer.draw(figure, canvas)
            }
        }
    }

}

