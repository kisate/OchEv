package com.example.ochev.viewclasses

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.ochev.baseclasses.dataclasses.Point

class DrawLinesView(
    context: Context?,
    attrs: AttributeSet? = null
) : View(context, attrs) {


    private val paint: Paint = Paint()

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4f
        paint.color = Color.parseColor("#FFC107")
    }

    private var currentLines: MutableList<Pair<Point, Point>> = ArrayList()

    fun invalidate(newCurrentLines: MutableList<Pair<Point, Point>>) {
        currentLines = newCurrentLines
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        drawLinesOnCanvas(currentLines, canvas)
    }

    fun drawLinesOnCanvas(currentLines: MutableList<Pair<Point, Point>>, canvas: Canvas?) {
        for (segment in currentLines) {
            drawLine(segment, canvas)
        }
    }

    private fun drawLine(segment: Pair<Point, Point>, canvas: Canvas?) {
        canvas?.drawLine(
            segment.first.x,
            segment.first.y,
            segment.second.x,
            segment.second.y,
            paint
        )
    }


}