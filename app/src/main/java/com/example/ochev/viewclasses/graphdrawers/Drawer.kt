package com.example.ochev.viewclasses.graphdrawers

import android.graphics.Canvas
import android.graphics.Paint
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.vertexfigures.VertexFigure
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

    fun drawText(
        figure: Figure,
        drawingInformation: DrawingInformation,
        canvas: Canvas?,
        fontPaint: Paint
    ) {
        if (figure is VertexFigure) {
            val textDrawingInformation = TextDrawingInformation(figure, drawingInformation.text, fontPaint)
            canvas?.drawText(drawingInformation.text, 0, drawingInformation.text.length, textDrawingInformation.x, textDrawingInformation.y, textDrawingInformation.paint)
        }
    }
}