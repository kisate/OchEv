package com.example.ochev.viewclasses.graphdrawers

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.Layout
import android.text.StaticLayout
import android.text.TextDirectionHeuristics
import android.text.TextPaint
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
                val staticLayout = generateStaticLayout(figure, drawingInformation)

                Log.d("text", staticLayout.height.toString())

                canvas?.withTranslation(bounds.left.toFloat(), bounds.bottom.toFloat()) {
                    staticLayout.draw(canvas)
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
        paint.textSize = 50f
        var staticLayout = buildStaticLayout(figure, drawingInformation, paint)
        val bounds = calcRect(figure)
        while (staticLayout.height < abs(bounds.height())) {
            paint.textSize++
            staticLayout = buildStaticLayout(figure, drawingInformation, paint)
            Log.d("text", staticLayout.height.toString())
        }
        while (staticLayout.height > abs(bounds.height())) {
            paint.textSize--
            staticLayout = buildStaticLayout(figure, drawingInformation, paint)
            Log.d("text", "${staticLayout.height.toString()} ${bounds.height()}")
        }

        return staticLayout
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
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setTextDirection(TextDirectionHeuristics.LTR)
            .setLineSpacing(0f, 1f)
            .setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED)
            .build()
    }
}