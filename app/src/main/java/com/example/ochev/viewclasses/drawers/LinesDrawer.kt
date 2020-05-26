package com.example.ochev.viewclasses.drawers

import android.graphics.Canvas
import com.example.ochev.baseclasses.dataclasses.LineSegment
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.viewclasses.DrawLinesView

class LinesDrawer {

    lateinit var linesView: DrawLinesView

    fun invalidate(segment: LineSegment? ) {
        linesView.invalidate(segment)
    }

    fun drawLinesOnCanvas(currentLines: MutableList<LineSegment?>, canvas: Canvas?) {
        linesView.drawLinesOnCanvas(currentLines, canvas)
    }

    fun clear() {
        invalidate(null)
    }

}
