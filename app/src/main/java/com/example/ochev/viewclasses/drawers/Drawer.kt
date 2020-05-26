package com.example.ochev.viewclasses.drawers

import android.graphics.Canvas
import android.graphics.Rect
import android.text.*
import androidx.core.graphics.withTranslation
import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Vector
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Circle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rectangle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rhombus
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure
import com.example.ochev.viewclasses.drawers.drawinginformations.DrawingInformation
import com.example.ochev.viewclasses.drawers.drawinginformations.DrawingMode
import kotlin.math.*

abstract class Drawer {


    abstract fun draw(
        figure: Figure,
        drawingInformation: DrawingInformation,
        canvas: Canvas?
    )

    fun drawEditingPoints(
        drawingInformation: DrawingInformation,
        points: MutableList<Point>,
        figure: Figure,
        canvas: Canvas?
    ) {
        if (drawingInformation.drawingMode != DrawingMode.EDIT) return

        val drawingInfo = DrawingInformation.getEdgeDrawingInformation()
        drawingInfo?.enterMode(DrawingMode.EDIT_CORNERS)

        for (point in points) {
            drawingInfo?.style?.circuitPaint?.let { canvas?.drawCircle(point.x, point.y, 12f, it) }
        }

        drawingInfo?.style?.circuitPaint?.let { canvas?.drawCircle(figure.center.x, figure.center.y, 12f, it) }



    }

    fun drawMultiLineText(
        figure: Figure,
        drawingInformation: DrawingInformation,
        canvas: Canvas?
    ) {

        if (figure is VertexFigure) {
            if (drawingInformation.text.isNotEmpty()) {

                val bounds = calcRect(figure)
                if (abs(bounds.height()) > MIN_BOUNDS_HEIGHT) {
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
                val component = (figure.radius / sqrt(2.0)).toFloat()

                val leftBottomCorner = figure.center.movedByVector(Vector(-component, -component))
                val rightTopCorner = figure.center.movedByVector(Vector(component, component))

                return Rect(
                    leftBottomCorner.x.toInt(),
                    rightTopCorner.y.toInt(),
                    rightTopCorner.x.toInt(),
                    leftBottomCorner.y.toInt()
                )
            }
            is Rhombus -> {
                return Rect(
                    Point.centre(mutableListOf(figure.leftCorner, figure.upCorner)).x.toInt(),
                    Point.centre(mutableListOf(figure.leftCorner, figure.upCorner)).y.toInt(),
                    Point.centre(mutableListOf(figure.rightCorner, figure.upCorner)).x.toInt(),
                    Point.centre(mutableListOf(figure.rightCorner, figure.downCorner)).y.toInt()
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
        val paint = TextPaint(drawingInformation.style.fontPaint)
        paint.textSize = DEFAULT_TEXT_SIZE
        val bounds = calcRect(figure)

        var count = 0

        var approximatedHeight = approximateHeight(paint, drawingInformation, bounds)

        paint.textSize = DEFAULT_TEXT_SIZE * abs(bounds.height()) / approximatedHeight
        approximatedHeight = approximateHeight(paint, drawingInformation, bounds)

        while (approximatedHeight < abs(bounds.height()) && paint.textSize < MAX_TEXT_SIZE) {
            if (paint.textSize * sqrt(abs(bounds.height()) / approximatedHeight) - paint.textSize < 1) paint.textSize++
            else paint.textSize *= sqrt(abs(bounds.height()) / approximatedHeight)
            approximatedHeight = approximateHeight(paint, drawingInformation, bounds)
            count++
        }
        while (approximatedHeight > abs(bounds.height()) && paint.textSize > MIN_TEXT_SIZE) {
            if (paint.textSize - paint.textSize * sqrt(abs(bounds.height()) / approximatedHeight) < 1) paint.textSize--
            else paint.textSize *= sqrt(abs(bounds.height()) / approximatedHeight)
            approximatedHeight = approximateHeight(paint, drawingInformation, bounds)
            count++
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

    private fun approximateHeight(
        paint: TextPaint,
        drawingInformation: DrawingInformation,
        bounds: Rect
    ): Float {

        val lines = paint.measureText(drawingInformation.text) / abs(bounds.width())

        val fontSize = abs(paint.fontMetrics.ascent - paint.fontMetrics.descent)

        val spacingHeight = ceil(lines) * fontSize * max(
            MIN_LINE_SIZE_COEFFICIENT,
            (LINE_SIZE_COEFFICIENT - ceil(lines) * LINE_SIZE_COEFFICIENT_DELTA)
        )

        return fontSize * lines + spacingHeight
    }

    companion object {
        private const val MIN_BOUNDS_HEIGHT = 10f
        private const val DEFAULT_TEXT_SIZE = 50f
        private const val MAX_TEXT_SIZE = 2000f
        private const val MIN_TEXT_SIZE = 0.5f
        private const val LINE_SIZE_COEFFICIENT = 1f
        private const val LINE_SIZE_COEFFICIENT_DELTA = 0.13f
        private const val MIN_LINE_SIZE_COEFFICIENT = 0.2f
    }
}