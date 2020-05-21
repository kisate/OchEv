package com.example.ochev.viewclasses.graphdrawers

import android.graphics.Canvas
import com.example.ochev.baseclasses.Figure
import com.example.ochev.viewclasses.DrawingInformation

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