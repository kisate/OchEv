package com.example.ochev.viewclasses.drawers

import android.graphics.Canvas
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.viewclasses.DrawLinesView

class LinesDrawer {

    lateinit var linesView: DrawLinesView

    fun invalidate(segment: MutableList<Pair<Point, Point>> ) {
        linesView.invalidate(segment)
    }

    fun drawLinesOnCanvas(currentLines: MutableList<Pair<Point, Point>>, canvas: Canvas?) {
        linesView.drawLinesOnCanvas(currentLines, canvas)
    }

    fun clear() {
        invalidate(mutableListOf())
    }

}
