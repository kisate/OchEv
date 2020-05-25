package com.example.ochev.viewclasses.graphdrawers

import android.graphics.Canvas
import android.graphics.Rect
import android.text.*
import android.util.Log
import androidx.core.graphics.withTranslation
import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Circle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rectangle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure
import com.example.ochev.viewclasses.DrawingInformation
import kotlin.math.*

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

    fun drawMultiLineText(
        figure: Figure,
        drawingInformation: DrawingInformation,
        canvas: Canvas?
    ) {

        if (figure is VertexFigure) {
            if (drawingInformation.text.isNotEmpty()) {

                val bounds = calcRect(figure)
                if (abs(bounds.height()) > MIN_BOUNDS_HEIGHT)
                {
                    val staticLayout = generateStaticLayout(figure, drawingInformation)

                    canvas?.withTranslation(bounds.left.toFloat(), bounds.bottom.toFloat()) {
                        staticLayout.draw(canvas)
                    }
                }
            }
        }
    }

    private fun calcRect(figure: VertexFigure): Rect {
        when (figure) {
            is Rectangle -> {
                return Rect(
                    min(figure.leftDownCorner.x, figure.rightDownCorner.x).toInt(),
                    max(figure.leftDownCorner.y, figure.leftUpCorner.y).toInt(),
                    max(figure.leftDownCorner.x, figure.rightDownCorner.x).toInt(),
                    min(figure.leftDownCorner.y, figure.leftUpCorner.y).toInt()
                )
            }
            is Circle -> {
                return Rect(
                    0,
                    (2 * sin(Math.PI / 180f * 45f) * figure.radius).toInt(),
                    (2 * cos(Math.PI / 180f * 45f) * figure.radius).toInt(),
                    0
                )

            }
            else -> {
                return Rect()
            }
        }
    }

    private fun generateStaticLayout(
        figure: VertexFigure,
        drawingInformation: DrawingInformation
    ): StaticLayout {
        val paint = TextPaint(styles[drawingInformation.currentStyle].fontPaint)
        paint.textSize = DEFAULT_TEXT_SIZE
        val bounds = calcRect(figure)

        while (paint.measureText(drawingInformation.text)/abs(bounds.width())*abs(paint.fontMetrics.top - paint.fontMetrics.bottom)* HEURISTIC_CONST < abs(bounds.height()))
        {
            paint.textSize++
            Log.d("text", "${paint.fontMetrics.top - paint.fontMetrics.bottom} ${paint.textSize}")
        }
        while (paint.measureText(drawingInformation.text)/abs(bounds.width())*abs(paint.fontMetrics.top - paint.fontMetrics.bottom)* HEURISTIC_CONST > abs(bounds.height()))
        {
            paint.textSize--
            Log.d("text", "${paint.fontMetrics.top - paint.fontMetrics.bottom} ${paint.textSize}")
        }

        paint.textSize = max(MIN_TEXT_SIZE, min(MAX_TEXT_SIZE, paint.textSize))

        return buildStaticLayout(figure, drawingInformation, paint)
    }

    private fun buildStaticLayout(
        figure: VertexFigure,
        drawingInformation: DrawingInformation,
        paint: TextPaint
    ): StaticLayout {
        val bounds = calcRect(figure)
        return StaticLayout.Builder
            .obtain(
                drawingInformation.text,
                0,
                drawingInformation.text.length,
                paint,
                bounds.width()
            )
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .setTextDirection(TextDirectionHeuristics.LTR)
            .setLineSpacing(0f, 1f)
            .setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED)
            .build()
    }

    companion object {
        private const val MIN_BOUNDS_HEIGHT = 10f
        private const val DEFAULT_TEXT_SIZE = 50f
        private const val MAX_TEXT_SIZE = 200f
        private const val MIN_TEXT_SIZE = 10f
        private const val HEURISTIC_CONST = 1.5f
    }
}