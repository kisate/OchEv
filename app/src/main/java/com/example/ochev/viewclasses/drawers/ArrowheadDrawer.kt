package com.example.ochev.viewclasses.drawers

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.ochev.algorithms.ArrowGetter
import com.example.ochev.baseclasses.dataclasses.Point

open class ArrowheadDrawer {

    var style: FigureStyle = FigureStyle()

    init {
        style.fillPaint.style = Paint.Style.FILL_AND_STROKE
        style.fillPaint.strokeWidth = 10f
        style.fillPaint.color = Color.BLACK
    }

    fun draw(
        from: Point,
        to: Point,
        canvas: Canvas?
    ) {
        canvas?.drawPath(ArrowGetter().getPathOfArrow(from, to), style.fillPaint)
    }


}