package com.example.ochev.viewclasses.graphdrawers.drawinginformations

import android.graphics.Color
import android.graphics.Paint
import com.example.ochev.viewclasses.graphdrawers.FigureStyle

class EdgeDrawingInformation : DrawingInformation {
    override var drawingMode: DrawingMode = DrawingMode.DEFAULT
    override var text: String = ""
    override var style: FigureStyle = FigureStyle()

    init {
        enterMode(DrawingMode.DEFAULT)
    }

    override fun enterMode(newDrawingMode: DrawingMode) {
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
                style.fillPaint.style = Paint.Style.FILL
                style.fillPaint.strokeWidth = 0f
                style.fillPaint.color = Color.GRAY
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