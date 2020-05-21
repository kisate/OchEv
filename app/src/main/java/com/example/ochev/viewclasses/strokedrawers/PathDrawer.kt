package com.example.ochev.viewclasses.strokedrawers

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path

class PathDrawer {
    private val paint = Paint()

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f
    }

    fun draw(path: Path, canvas: Canvas?) {
        canvas?.drawPath(path, paint)
    }

    fun setWidth(width: Float) {
        paint.strokeWidth = width
    }

}