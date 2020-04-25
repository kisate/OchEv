package com.example.ochev.viewclasses

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.PointInteractor
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.timeinteractors.Throttle
import com.example.ochev.baseclasses.vertexfigures.Circle
import java.util.*

@SuppressLint("ViewConstructor")
class StrokeInputView(
    context: Context?,
    attrs: AttributeSet? = null,
    drawStrokeView: DrawStrokeView,
    drawFiguresView: DrawGraphView
) :
    View(context, attrs) {

    private val inputHandler = InputHandler(drawStrokeView, drawFiguresView)
    private val throttle = Throttle(2)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var point: Point? = null
        throttle.attempt(Runnable { point = Point(event.x.toInt(), event.y.toInt()) })
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                inputHandler.touchStart(point)
            }
            MotionEvent.ACTION_MOVE -> {
                inputHandler.touchMove(point)
            }
            MotionEvent.ACTION_UP -> {
                inputHandler.touchUp()
            }
        }
        return true
    }
}

class InputHandler(
    private val drawStrokeView: DrawStrokeView,
    private val drawGraphView: DrawGraphView
) {
    private var drawStrokeInteractor = drawStrokeView.drawStrokeInteractor
    private var stroke: Stroke = Stroke()
    private lateinit var lastPoint: Point

    fun touchMove(point: Point?) {
        if (point == null) return
        if (PointInteractor().distance(point, lastPoint) <= 10f) return
        stroke.addPoint(point)
        drawStrokeInteractor.set(drawStrokeView, stroke)
        lastPoint = point
    }

    fun touchUp() {

        //drawGraphView.graph.modifyByStrokes(mutableListOf(stroke))

        Log.println(Log.DEBUG, "dbgCountOfPointInStroke", stroke.points.size.toString())

        drawGraphView.graph.addVertex(Circle(Point(Random().nextInt(500)+50, Random().nextInt(500)+50), Random().nextInt(20)+10))

        drawGraphView.invalidate()
        stroke.points.clear()
        drawStrokeInteractor.clear(drawStrokeView)
    }

    fun touchStart(point: Point?) {
        if (point == null) return
        lastPoint = point
        stroke.addPoint(point)
        drawStrokeInteractor.set(drawStrokeView, stroke)
    }
}