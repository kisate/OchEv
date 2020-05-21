package com.example.ochev.viewclasses.graphdrawers

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.vertexfigures.Rectangle
import com.example.ochev.viewclasses.DrawingInformation
import com.example.ochev.viewclasses.DrawingMode

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