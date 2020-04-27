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
import com.example.ochev.baseclasses.vertexfigures.Vertexes
import com.example.ochev.ml.Classifier
import com.example.ochev.ml.Utils
import com.google.android.gms.tasks.Tasks
import java.util.concurrent.Callable


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

    fun clear() {
        inputHandler.clear()
    }

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
    private val drawGraphView: DrawGraphView,
    private val classifier: Classifier
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

        val bitmap = Utils.loadBitmapFromView(drawStrokeView)

        val information = InfrormationForNormalizer(
            classifier,
            bitmap,
            drawGraphView.graph,
            mutableListOf(stroke.copy())
        )

        Tasks.call(
            MainActivity.Executor.executorService,
            Callable<Figure?> { drawGraphView.graph.modifyByStrokes(information) })
            .addOnSuccessListener { figure -> Log.d("Modify", "Classified as $figure") }
            .addOnFailureListener {e -> Log.e("Modify", "Error modifying", e)}

//        drawGraphView.graph.modifyByStrokes(information)

        Log.println(Log.DEBUG, "dbgCountOfPointInStroke", stroke.points.size.toString())


        drawGraphView.invalidate()
        stroke = Stroke()

        /* val bitmap = Utils.loadBitmapFromView(drawStrokeView)

         if ((bitmap != null) && classifier.isInitialized) {
             classifier
                 .classifyAsync(bitmap, stroke)
                 .addOnSuccessListener { result ->
                     Toast.makeText(
                         classifier.context,
                         result.toString(), LENGTH_LONG
                     ).show()
                 }
                 .addOnFailureListener { e -> Log.e("Classify", "Error classifying", e) }
         }*/
        drawStrokeInteractor.clear(drawStrokeView)
    }

    fun touchStart(point: Point?) {
        if (point == null) return
        lastPoint = point
        stroke.addPoint(point)
        drawStrokeInteractor.set(drawStrokeView, stroke)
    }

    fun clear() {
        drawGraphView.clear()
        drawStrokeInteractor.clear(drawStrokeView)
    }
}