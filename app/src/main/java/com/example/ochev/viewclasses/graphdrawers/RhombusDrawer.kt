package com.example.ochev.viewclasses.graphdrawers

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rectangle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rhombus
import com.example.ochev.viewclasses.DrawingInformation
import com.example.ochev.viewclasses.DrawingMode

class RhombusDrawer: Drawer() {

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
        styles[DrawingMode.DEFAULT.ordinal].fontPaint.style = Paint.Style.FILL
        styles[DrawingMode.DEFAULT.ordinal].fontPaint.isAntiAlias = true
        styles[DrawingMode.DEFAULT.ordinal].fontPaint.color = Color.BLACK

        /*
            editing style of rectangles
         */
        styles[DrawingMode.EDIT.ordinal].fillPaint.style = Paint.Style.FILL
        styles[DrawingMode.EDIT.ordinal].fillPaint.strokeWidth = 0f
        styles[DrawingMode.EDIT.ordinal].fillPaint.color = Color.GRAY
        styles[DrawingMode.EDIT.ordinal].circuitPaint.style = Paint.Style.STROKE
        styles[DrawingMode.EDIT.ordinal].circuitPaint.strokeWidth = 10f
        styles[DrawingMode.EDIT.ordinal].circuitPaint.color = Color.BLACK
        styles[DrawingMode.EDIT.ordinal].fontPaint.style = Paint.Style.FILL
        styles[DrawingMode.EDIT.ordinal].fontPaint.isAntiAlias = true
        styles[DrawingMode.EDIT.ordinal].fontPaint.color = Color.parseColor("#FFC107")
        /*
            editing corner style of rectangles
         */
        styles[DrawingMode.EDIT_CORNERS.ordinal].circuitPaint.style = Paint.Style.FILL_AND_STROKE
        styles[DrawingMode.EDIT_CORNERS.ordinal].circuitPaint.strokeWidth = 3f
        styles[DrawingMode.EDIT_CORNERS.ordinal].circuitPaint.color = Color.parseColor("#FFC107")


    }

    fun drawRhombus(rhombus: Rhombus, canvas: Canvas?, paint: Paint) {
        val path = Path()
        path.moveTo(rhombus.leftCorner.x, rhombus.leftCorner.y)
        path.lineTo(rhombus.upCorner.x, rhombus.upCorner.y)
        path.lineTo(rhombus.rightCorner.x, rhombus.rightCorner.y)
        path.lineTo(rhombus.downCorner.x, rhombus.downCorner.y)
        path.close()
        canvas?.drawPath(path, paint)
    }

    override fun draw(figure: Figure, drawingInformation: DrawingInformation, canvas: Canvas?) {
        figure as Rhombus
        drawRhombus(figure, canvas, styles[drawingInformation.currentStyle].fillPaint)
        drawRhombus(figure, canvas, styles[drawingInformation.currentStyle].circuitPaint)
        val textDrawingInformation = TextDrawingInformation(figure, drawingInformation.text, styles[drawingInformation.currentStyle].fontPaint)
        drawMultiLineText(figure, drawingInformation, canvas)
//        canvas?.drawText(drawingInformation.text, 0, drawingInformation.text.length, textDrawingInformation.x, textDrawingInformation.y, textDrawingInformation.paint)
        if (drawingInformation.drawingMode == DrawingMode.EDIT) {
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

}
