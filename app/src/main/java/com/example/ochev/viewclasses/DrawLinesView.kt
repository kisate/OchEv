package com.example.ochev.viewclasses

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.ochev.baseclasses.dataclasses.LineSegment

class DrawLinesView(
    context: Context?,
    attrs: AttributeSet? = null
) : View(context, attrs) {


    private val paint: Paint = Paint()

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 7f
        paint.color = Color.parseColor("#FFC107")
    }

    private var currentLines: LineSegment? = null

    fun invalidate(segment: LineSegment?) {
        currentLines = segment
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        drawLinesOnCanvas(mutableListOf(currentLines), canvas)
    }

    fun drawLinesOnCanvas(currentLines: MutableList<LineSegment?>, canvas: Canvas?) {
        for (segment in currentLines) {

            drawLine(segment, canvas)
        }
    }

    private fun drawLine(segment: LineSegment?, canvas: Canvas?) {
        if (segment == null) return
        canvas?.drawLine(
            segment.A.x,
            segment.A.y,
            segment.B.x,
            segment.B.y,
            paint
        )
    }


}