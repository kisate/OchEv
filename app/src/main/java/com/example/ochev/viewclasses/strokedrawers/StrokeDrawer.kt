package com.example.ochev.viewclasses.strokedrawers

import android.util.Log
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.viewclasses.DrawStrokeView

class StrokeDrawer(
    val drawStrokeView: DrawStrokeView
) {
    val stroke = Stroke()

    fun add(x: Float, y: Float) {
        Log.i("StrokeDrawer", "$x $y")
        val point = Point(x, y)
        if (stroke.points.size == 0) {
            drawStrokeView.path.moveTo(x, y)
        } else {
            drawStrokeView.path.lineTo(x, y)
        }
        stroke.addPoint(point)
        drawStrokeView.invalidate()
    }

    fun clear() {
        drawStrokeView.path.reset()
        stroke.points.clear()
        drawStrokeView.invalidate()
    }
}