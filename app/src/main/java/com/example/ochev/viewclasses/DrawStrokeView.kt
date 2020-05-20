package com.example.ochev.viewclasses

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke


class DrawStrokeView(
    context: Context?,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    val path = Path()
    val pathDrawer = PathDrawer()

    override fun onDraw(canvas: Canvas?) {
        pathDrawer.draw(path, canvas)
    }
}

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

class StrokeDrawer(
    private val drawStrokeView: DrawStrokeView
) {
    private val stroke = Stroke()

    fun add(x: Float, y: Float) {
        Log.i("StrokeDrawer", x.toString() + " " + y.toString())
        val point = Point(x.toInt(), y.toInt())
        if (stroke.points.size == 0) {
            drawStrokeView.path.moveTo(x, y)
        }
        else {
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