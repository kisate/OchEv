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
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.edgefigures.Line
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor
import com.example.ochev.baseclasses.vertexfigures.Circle
import com.example.ochev.baseclasses.vertexfigures.Rectangle

class DrawGraphView(
    context: Context?,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    val graphEditor = GraphEditor()
    val drawGraphInteractor = DrawGraphInteractor()

//    fun clear() {
//        graph.vertexes.clear()
//        graph.edges.clear()
//        invalidate()
//    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawColor(Color.LTGRAY)
//        for (figure in graphEditor.figuresSortedByHeights) {
//            drawGraphInteractor.draw(figure, canvas)
//        }
    }
}

class FigureStyle {
    val fontPaint = Paint()
    val circuitPaint = Paint()
    val fillPaint = Paint()
}

abstract class Drawer {
    var currentStyle = 0
    val styles: MutableList<FigureStyle> = ArrayList(3)

    init {
        styles.add(FigureStyle())
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

    abstract fun draw(figure: Figure, canvas: Canvas?)
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
        styles[DrawingMode.EDIT.ordinal].fillPaint.color = Color.GRAY
        styles[DrawingMode.EDIT.ordinal].circuitPaint.style = Paint.Style.STROKE
        styles[DrawingMode.EDIT.ordinal].circuitPaint.strokeWidth = 10f
        styles[DrawingMode.EDIT.ordinal].circuitPaint.color = Color.BLACK
        /*
            editing corner style of circles
         */
        styles[DrawingMode.EDIT_CORNERS.ordinal].circuitPaint.style = Paint.Style.FILL_AND_STROKE
        styles[DrawingMode.EDIT_CORNERS.ordinal].circuitPaint.strokeWidth = 3f
        styles[DrawingMode.EDIT_CORNERS.ordinal].circuitPaint.color = Color.parseColor("#FFC107")
    }

    override fun draw(figure: Figure, canvas: Canvas?) {

        figure as Circle

        canvas?.drawCircle(
            figure.center.x.toFloat(),
            figure.center.y.toFloat(),
            figure.radius.toFloat(),
            styles[currentStyle].fillPaint
        )
        canvas?.drawCircle(
            figure.center.x.toFloat(),
            figure.center.y.toFloat(),
            figure.radius.toFloat(),
            styles[currentStyle].circuitPaint
        )
        if (figure.drawingInformation.drawingMode == DrawingMode.EDIT)
            for (point in figure.getMovingPoints()) {
                canvas?.drawCircle(
                    point.x.toFloat(),
                    point.y.toFloat(),
                    5f,
                    styles[DrawingMode.EDIT_CORNERS.ordinal].circuitPaint
                )
            }
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
        styles[DrawingMode.EDIT.ordinal].fillPaint.color = Color.GRAY
        styles[DrawingMode.EDIT.ordinal].circuitPaint.style = Paint.Style.STROKE
        styles[DrawingMode.EDIT.ordinal].circuitPaint.strokeWidth = 10f
        styles[DrawingMode.EDIT.ordinal].circuitPaint.color = Color.BLACK
        /*
            editing corner style of rectabgles
         */
        styles[DrawingMode.EDIT_CORNERS.ordinal].circuitPaint.style = Paint.Style.FILL_AND_STROKE
        styles[DrawingMode.EDIT_CORNERS.ordinal].circuitPaint.strokeWidth = 3f
        styles[DrawingMode.EDIT_CORNERS.ordinal].circuitPaint.color = Color.parseColor("#FFC107")
    }

    fun drawRect(rect: Rectangle, canvas: Canvas?, paint: Paint) {
        val path = Path()
        path.moveTo(rect.rightDownCorner.x.toFloat(), rect.rightDownCorner.y.toFloat())
        path.lineTo(rect.rightUpCorner.x.toFloat(), rect.rightUpCorner.y.toFloat())
        path.lineTo(rect.leftUpCorner.x.toFloat(), rect.leftUpCorner.y.toFloat())
        path.lineTo(rect.leftDownCorner.x.toFloat(), rect.leftDownCorner.y.toFloat())
        path.close()
        canvas?.drawPath(path, paint)
    }

    override fun draw(figure: Figure, canvas: Canvas?) {
        figure as Rectangle
        drawRect(figure, canvas, styles[currentStyle].fillPaint)
        drawRect(figure, canvas, styles[currentStyle].circuitPaint)
        if (figure.drawingInformation.drawingMode == DrawingMode.EDIT)
            for (point in figure.getMovingPoints()) {
                canvas?.drawCircle(
                    point.x.toFloat(),
                    point.y.toFloat(),
                    5f,
                    styles[DrawingMode.EDIT_CORNERS.ordinal].circuitPaint
                )
            }
    }
}

class LineDrawer : Drawer() {

    init {
        /*
            default style of lines
         */
        styles[DrawingMode.DEFAULT.ordinal].circuitPaint.style = Paint.Style.STROKE
        styles[DrawingMode.DEFAULT.ordinal].circuitPaint.color = Color.BLACK
        styles[DrawingMode.DEFAULT.ordinal].circuitPaint.strokeWidth = 10f
        styles[DrawingMode.DEFAULT.ordinal].fontPaint.style = Paint.Style.FILL
        styles[DrawingMode.DEFAULT.ordinal].fontPaint.color = Color.BLACK
        styles[DrawingMode.DEFAULT.ordinal].fontPaint.textSize = 30f
        /*
            editing style of lines
         */
        styles[DrawingMode.EDIT.ordinal].circuitPaint.style = Paint.Style.STROKE
        styles[DrawingMode.EDIT.ordinal].circuitPaint.color = Color.DKGRAY
        styles[DrawingMode.EDIT.ordinal].circuitPaint.strokeWidth = 10f

    }

    override fun draw(figure: Figure, canvas: Canvas?) {

        figure as Line

        val path = Path()
        val from = figure.beginFigure.center
        val to = figure.endFigure.center

        path.moveTo(from.x.toFloat(), from.y.toFloat())
        path.lineTo(to.x.toFloat(), to.y.toFloat())
        canvas?.drawPath(path, styles[currentStyle].circuitPaint)
//        canvas?.drawTextOnPath("Test Text 1234567", path, 100f, 100f, styles[DrawingMode.DEFAULT.ordinal].fontPaint)
    }
}

class DrawGraphInteractor {
    val circleDrawer = CircleDrawer()
    val rectangleDrawer = RectangleDrawer()
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
            is Line -> {
                lineDrawer.currentStyle = figure.drawingInformation.drawingMode.ordinal
                lineDrawer.draw(figure, canvas)
            }
        }
    }

}

