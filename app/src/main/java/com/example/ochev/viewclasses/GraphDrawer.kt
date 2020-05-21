package com.example.ochev.viewclasses

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.dataclasses.InformationForNormalizer
import com.example.ochev.baseclasses.edgefigures.Edge
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor
import com.example.ochev.baseclasses.vertexfigures.Circle
import com.example.ochev.baseclasses.vertexfigures.Rectangle

class DrawGraphView(
    context: Context?,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    val graphEditor = GraphEditor()
    val figuresDrawer = FiguresDrawer()
    var scale = 1f

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawColor(Color.LTGRAY)
        for (figure in graphEditor.graph.figures.figuresSortedByHeights) {
            Log.i("ClassifyDbg", figure.toString())
            figuresDrawer.draw(figure.figure, figure.drawingInformation, canvas)
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

    abstract fun draw(figure: Figure, drawingInformation: DrawingInformation, canvas: Canvas?)
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

    override fun draw(figure: Figure, drawingInformation: DrawingInformation, canvas: Canvas?) {

        figure as Circle

        val drawCircle = canvas?.drawCircle(
            figure.center.x,
            figure.center.y,
            figure.radius,
            styles[currentStyle].fillPaint
        )
        canvas?.drawCircle(
            figure.center.x,
            figure.center.y,
            figure.radius,
            styles[currentStyle].circuitPaint
        )
        if (drawingInformation.drawingMode == DrawingMode.EDIT)
            for (point in figure.getMovingPoints()) {
                canvas?.drawCircle(
                    point.x,
                    point.y,
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
        Log.i("ClassifyDbg", rect.toString())
        Log.i("ClassifyDbg", rect.rightDownCorner.toString() + rect.leftUpCorner.toString())
        val path = Path()
        path.moveTo(rect.rightDownCorner.x, rect.rightDownCorner.y)
        path.lineTo(rect.rightUpCorner.x, rect.rightUpCorner.y)
        path.lineTo(rect.leftUpCorner.x, rect.leftUpCorner.y)
        path.lineTo(rect.leftDownCorner.x, rect.leftDownCorner.y)
        path.close()
        canvas?.drawPath(path, paint)
    }

    override fun draw(figure: Figure, drawingInformation: DrawingInformation, canvas: Canvas?) {
        figure as Rectangle
        drawRect(figure, canvas, styles[currentStyle].fillPaint)
        drawRect(figure, canvas, styles[currentStyle].circuitPaint)
        if (drawingInformation.drawingMode == DrawingMode.EDIT)
            for (point in figure.getMovingPoints()) {
                canvas?.drawCircle(
                    point.x,
                    point.y,
                    5f,
                    styles[DrawingMode.EDIT_CORNERS.ordinal].circuitPaint
                )
            }
    }
}

class EdgeDrawer : Drawer() {

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

    override fun draw(figure: Figure, drawingInformation: DrawingInformation, canvas: Canvas?) {

        figure as Edge

        Log.i("EdgeDrawingDbg", figure.toString())

        val path = Path()
        val from = figure.beginFigure.center
        val to = figure.endFigure.center

        path.moveTo(from.x, from.y)
        path.lineTo(to.x, to.y)
        canvas?.drawPath(path, styles[currentStyle].circuitPaint)
//        canvas?.drawTextOnPath("Test Text 1234567", path, 100f, 100f, styles[DrawingMode.DEFAULT.ordinal].fontPaint)
    }
}

class FiguresDrawer {
    val circleDrawer = CircleDrawer()
    val rectangleDrawer = RectangleDrawer()
    val edgeDrawer = EdgeDrawer()

    fun draw(figure: Figure, drawingInformation: DrawingInformation, canvas: Canvas?) {
        when (figure) {
            is Circle -> {
                circleDrawer.currentStyle = drawingInformation.drawingMode.ordinal
                circleDrawer.draw(figure, drawingInformation,canvas)
            }
            is Rectangle -> {
                rectangleDrawer.currentStyle = drawingInformation.drawingMode.ordinal
                rectangleDrawer.draw(figure, drawingInformation, canvas)
            }
            is Edge -> {
                edgeDrawer.currentStyle = drawingInformation.drawingMode.ordinal
                edgeDrawer.draw(figure, drawingInformation, canvas)
            }
        }
    }
}

class GraphDrawer(
    val graphView: DrawGraphView
) {
    fun clear(){
        graphView.graphEditor.clear()
        graphView.invalidate()
    }

    fun modifyByStrokes(information: InformationForNormalizer) {
        graphView.graphEditor.modifyByStrokes(information)
    }

}
