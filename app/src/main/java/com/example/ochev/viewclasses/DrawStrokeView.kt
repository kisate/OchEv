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
        paint.setPathEffect(CornerPathEffect(50f))
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawColor(Color.WHITE)
        canvas?.drawPath(path, paint)
    }
}

class DrawStrokeInteractor {

    private var lastId = 0

    fun add(drawStrokeView: DrawStrokeView, stroke: Stroke) {
        for (id in lastId until stroke.points.size) {
            drawStrokeView.path.lineTo(
                stroke.points[id].x.toFloat(),
                stroke.points[id].y.toFloat()
            )
        }
        drawStrokeView.invalidate()
        lastId = stroke.points.size
    }

    fun clear(drawStrokeView: DrawStrokeView) {
        drawStrokeView.path.reset()
        drawStrokeView.invalidate()
        lastId = 0
    }

}