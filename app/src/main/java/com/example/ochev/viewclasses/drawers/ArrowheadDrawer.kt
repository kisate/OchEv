package com.example.ochev.viewclasses.drawers

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.ochev.algorithms.ArrowGetter
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.viewclasses.drawers.drawinginformations.EdgeDrawingInformation

class ArrowheadDrawer {

    var style: FigureStyle = FigureStyle()

    init {
        style.circuitPaint.style = Paint.Style.STROKE
        style.circuitPaint.strokeWidth = 10f
        style.circuitPaint.color = Color.BLACK
    }

    fun draw(from: Point, to: Point, canvas: Canvas?) {
        canvas?.drawPath(ArrowGetter().getPathOfArrow(from, to), style.circuitPaint)
    }


}