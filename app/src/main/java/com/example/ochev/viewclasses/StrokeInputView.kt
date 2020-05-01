package com.example.ochev.viewclasses

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.ochev.MainActivity
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.dataclasses.InfrormationForNormalizer
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.PointInteractor
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.timeinteractors.Throttle
import com.example.ochev.ml.Classifier
import com.example.ochev.ml.Utils
import com.google.android.gms.tasks.Tasks
import java.util.concurrent.Callable

enum class InputMode(value: Int) {
    DRAWING(1),
    EDITING(2);
}

@SuppressLint("ViewConstructor")
class StrokeInputView(
    context: Context?,
    attrs: AttributeSet? = null,
    drawStrokeView: DrawStrokeView,
    drawFiguresView: DrawGraphView,
    classifier: Classifier
) :
    View(context, attrs) {

    private val inputHandler = InputHandler(drawStrokeView, drawFiguresView, classifier)
    private val throttle = Throttle(2)

    var inputMode = InputMode.DRAWING

    fun clear() {
        inputHandler.clear()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var point: Point? = null
        throttle.attempt(Runnable { point = Point(event.x.toInt(), event.y.toInt()) })
        if (inputMode == InputMode.DRAWING) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    inputHandler.touchStart(point)
                }
                MotionEvent.ACTION_MOVE -> {
                    inputHandler.touchMove(point)
                }
                MotionEvent.ACTION_UP -> {
                    inputHandler.touchUp(point)
                }
            }
        } else {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                }
                MotionEvent.ACTION_MOVE -> {
                }
                MotionEvent.ACTION_UP -> {
                }
            }
        }
        return true
    }
}

class InputHandler(
    private val drawStrokeView: DrawStrokeView,
    private val drawGraphView: DrawGraphView,
    private val classifier: Classifier
) {
    private var drawStrokeInteractor = drawStrokeView.drawStrokeInteractor
    private var stroke: Stroke = Stroke()
    private lateinit var firstPoint: Point
    private lateinit var lastPoint: Point
    private var lastTime = 0L


    fun touchMove(point: Point?) {
        if (point == null) return
        if (PointInteractor().distance(point, lastPoint) <= 15f) return
        stroke.addPoint(point)
        drawStrokeInteractor.set(drawStrokeView, stroke)
        lastPoint = point
    }

    fun touchUp(point: Point?) {
        if (point != null) {
            lastPoint = point
        }

        if (possibleEditModeEntry()) {
            Log.i("timeDebug", (System.nanoTime() - lastTime).toString())
            // check if points inside some figure

        }

        classifyStroke()
        stroke = Stroke()
        drawStrokeInteractor.clear(drawStrokeView)
    }

    fun touchStart(point: Point?) {
        if (point == null) return
        firstPoint = point
        lastTime = System.nanoTime()
        lastPoint = point
        stroke.addPoint(point)
        drawStrokeInteractor.set(drawStrokeView, stroke)
    }

    fun possibleEditModeEntry(): Boolean {
        return (System.nanoTime() - lastTime)/1000000f <= 300 && PointInteractor().distance(lastPoint, firstPoint) <= 50f
    }

    fun classifyStroke() {
        val bitmap = Utils.loadBitmapFromView(drawStrokeView)

        val information = InfrormationForNormalizer(
            classifier,
            bitmap,
            drawGraphView.graph,
            mutableListOf(stroke.copy())
        )

        Tasks.call(
            MainActivity.Executor.executorService,
            Callable<Figure?> {
                drawGraphView.graph.modifyByStrokes(information)
            })
            .addOnSuccessListener { figure ->
                Log.i("Modify", "Classified as $figure")
                drawGraphView.invalidate()
                if (figure == null)
                    Toast.makeText(drawGraphView.context, "Could not recognize", Toast.LENGTH_SHORT)
                        .show()
            }
            .addOnFailureListener { e -> Log.i("Modify", "Error modifying", e) }

        Log.i("dbgCountOfPointInStroke", stroke.points.size.toString())
    }

    fun clear() {
        drawGraphView.clear()
        drawStrokeInteractor.clear(drawStrokeView)
    }
}