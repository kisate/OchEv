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

    val path = Path()
    val drawStrokeInteractor = DrawStrokeInteractor()

    override fun onDraw(canvas: Canvas?) {
        drawStrokeInteractor.draw(path, canvas)
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
}

class DrawStrokeInteractor {

    private val pathDrawer = PathDrawer()

    fun set(drawStrokeView: DrawStrokeView, stroke: Stroke) {
        clear(drawStrokeView)
        if (stroke.points.size > 0) {
            drawStrokeView.path.moveTo(stroke.points[0].x.toFloat(), stroke.points[0].y.toFloat())
            //drawStrokeView.path.addCircle(stroke.points[0].x.toFloat(), stroke.points[0].y.toFloat(),5f, Path.Direction.CCW)
            for (id in 1 until stroke.points.size) {
                drawStrokeView.path.lineTo(
                    stroke.points[id].x.toFloat(),
                    stroke.points[id].y.toFloat()
                )
                //drawStrokeView.path.addCircle(stroke.points[id].x.toFloat(), stroke.points[id].y.toFloat(),5f, Path.Direction.CCW)
            }
        }
        drawStrokeView.invalidate()
    }

    fun clear(drawStrokeView: DrawStrokeView) {
        drawStrokeView.path.reset()
        drawStrokeView.invalidate()
    }

    fun draw(path: Path, canvas: Canvas?) {
        pathDrawer.draw(path, canvas)
    }

}