package com.example.ochev.viewclasses.drawers.drawinginformations

import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.PathEffect
import com.example.ochev.viewclasses.drawers.FigureStyle

class EdgeDrawingInformation : DrawingInformation {
    override var drawingMode: DrawingMode = DrawingMode.DEFAULT
    override var text: String = ""
    override var style: FigureStyle = FigureStyle()

    var types: MutableList<Int> = ArrayList()

    fun switchTypeToNext(index: Int) {
        types[index] = types[index] xor 1
    }

    init {
        types.add(0)
        types.add(1)
        enterMode(DrawingMode.DEFAULT)
    }

    override fun enterMode(newDrawingMode: DrawingMode) {
        style = FigureStyle()
        drawingMode = newDrawingMode
        when (newDrawingMode) {
            DrawingMode.DEFAULT -> {
                style.fillPaint.style = Paint.Style.FILL
                style.fillPaint.strokeWidth = 0f
                style.fillPaint.color = Color.WHITE
                style.circuitPaint.style = Paint.Style.STROKE
                style.circuitPaint.strokeWidth = 10f
                style.circuitPaint.color = Color.BLACK
                style.fontPaint.style = Paint.Style.FILL
                style.fontPaint.isAntiAlias = true
                style.fontPaint.color = Color.BLACK
            }
            DrawingMode.EDIT -> {
                style.circuitPaint.setPathEffect(DashPathEffect(floatArrayOf(30f, 10f), 20f));
                style.circuitPaint.style = Paint.Style.STROKE
                style.circuitPaint.strokeWidth = 10f
                style.circuitPaint.color = Color.BLACK
                style.fontPaint.style = Paint.Style.FILL
                style.fontPaint.isAntiAlias = true
                style.fontPaint.color = Color.parseColor("#FFC107")
            }
            DrawingMode.EDIT_CORNERS -> {
                style.circuitPaint.style = Paint.Style.FILL_AND_STROKE
                style.circuitPaint.strokeWidth = 3f
                style.circuitPaint.color = Color.parseColor("#FFC107")
            }
        }
    }
}