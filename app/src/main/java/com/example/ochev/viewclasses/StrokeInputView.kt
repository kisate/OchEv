package com.example.ochev.viewclasses

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.ochev.baseclasses.FigureNormalizer
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.PointInteractor
import com.example.ochev.baseclasses.dataclasses.Stroke

@SuppressLint("ViewConstructor")
class StrokeInputView(
    context: Context?,
    attrs: AttributeSet? = null,
    drawStrokeView: DrawStrokeView,
    drawFiguresView: DrawGraphView
) :
    View(context, attrs) {

    private val inputHandler = InputHandler(drawStrokeView, drawFiguresView)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val point = Point(event.x.toInt(), event.y.toInt())
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

    private val figureNormalizer = FigureNormalizer()
    private var drawStrokeInteractor = drawStrokeView.drawStrokeInteractor
    private var stroke: Stroke = Stroke()
    private lateinit var lastPoint: Point

    fun touchMove(point: Point) {
        if (PointInteractor().distance(point, lastPoint) <= 20f) return
        stroke.addPoint(point)
        drawStrokeInteractor.set(drawStrokeView, stroke)
        lastPoint = point
    }

    fun touchUp() {


        figureNormalizer.normaliseStrokes( mutableListOf(stroke), drawGraphView.graph)

        drawGraphView.invalidate()
        stroke.points.clear()
        drawStrokeInteractor.clear(drawStrokeView)
    }

    fun touchStart(point: Point) {
        lastPoint = point
        stroke.addPoint(point)
        drawStrokeInteractor.set(drawStrokeView, stroke)
    }
}