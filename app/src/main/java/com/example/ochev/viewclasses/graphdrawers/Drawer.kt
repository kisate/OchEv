package com.example.ochev.viewclasses.graphdrawers

import android.graphics.Canvas
import android.graphics.Paint
import com.example.ochev.baseclasses.Figure
import com.example.ochev.viewclasses.DrawingInformation

abstract class Drawer {
    val styles: MutableList<FigureStyle> = ArrayList(3)

    init {
        styles.add(FigureStyle())
        styles.add(FigureStyle())
        styles.add(FigureStyle())
    }

    abstract fun draw(
        figure: Figure,
        drawingInformation: DrawingInformation,
        canvas: Canvas?
    )

    abstract fun drawText(
        figure: Figure,
        drawingInformation: DrawingInformation,
        canvas: Canvas?,
        fontPaint: Paint
    )
}