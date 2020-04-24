package com.example.ochev.viewclasses

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.PointInteractor
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.edgefigures.Line
import com.example.ochev.baseclasses.vertexfigures.Circle
import com.example.ochev.baseclasses.vertexfigures.Rectangle
import com.example.ochev.baseclasses.vertexfigures.Triangle
import kotlin.random.Random

@SuppressLint("ViewConstructor")
class StrokeInputView(
    context: Context?,
    attrs: AttributeSet? = null,
    drawStrokeView: DrawStrokeView,
    drawFiguresView: DrawGraphView
) :
    View(context, attrs) {

    // public <-> ML
    val inputHandler = InputHandler(drawStrokeView, drawFiguresView)

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

    private var drawStrokeInteractor = DrawStrokeInteractor()
    private var drawGraphInteractor = DrawGraphInteractor()

    private var stroke: Stroke = Stroke()

    private lateinit var lastPoint: Point

    fun touchMove(point: Point) {
        if (PointInteractor().distance(point, lastPoint) <= 20f) return
        stroke.addPoint(point)
        drawStrokeInteractor.set(drawStrokeView, stroke)
        lastPoint = point
    }

    var cnt = 0

    fun touchUp() {
        // Дать stroke на разбор

        // Принять фигуру
        var figure: Figure? = null

        if (cnt == 0) {
            figure = Triangle(pointA = Point(200, 200), pointB = Point(200, 700), pointC = Point(500, 300))
        }
        else if (cnt == 1) {
            figure = Circle(center = Point(600, 600), radius = 100)
        }
        else if (cnt == 2) {
            figure = Rectangle(leftDownCorner = Point(200, 800), rightUpCorner = Point(500, 700))
        }
        else if (cnt == 3) {
            figure = Line(beginFigure = drawGraphView.graph.vertexes[0], endFigure = drawGraphView.graph.vertexes[1])
        }
        stroke.points.clear()
        drawStrokeInteractor.set(drawStrokeView, stroke)
        figure?.let { drawGraphInteractor.add(drawGraphView, it) }
        cnt++
    }

    fun touchStart(point: Point) {
        lastPoint = point
        stroke.addPoint(point)
        drawStrokeInteractor.set(drawStrokeView, stroke)
    }


}