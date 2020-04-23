package com.example.ochev.viewclasses

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.ochev.baseclasses.dataclasses.Stroke


class DrawStrokeView(
    context: Context?,
    attrs: AttributeSet? = null

) : View(context, attrs) {

    private val paint: Paint = Paint()

    var path = Path()

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawPath(path, paint)
    }
}

class DrawStrokeInteractor {

    fun set(drawStrokeView: DrawStrokeView, stroke: Stroke) {
        clear(drawStrokeView)
        if (stroke.points.size > 0) {
            drawStrokeView.path.moveTo(stroke.points[0].x.toFloat(), stroke.points[0].y.toFloat())
            for (id in 1 until stroke.points.size) {
                drawStrokeView.path.lineTo(
                    stroke.points[id].x.toFloat(),
                    stroke.points[id].y.toFloat()
                )
            }
        }
        drawStrokeView.invalidate()
    }

    fun clear(drawStrokeView: DrawStrokeView) {
        drawStrokeView.path.reset()
        drawStrokeView.invalidate()
    }

}