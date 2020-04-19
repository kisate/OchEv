package viewclasses

import android.R.attr.data
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.Log.DEBUG
import android.view.MotionEvent
import android.view.View
import baseclasses.dataclasses.Point
import baseclasses.dataclasses.PointInteractor
import baseclasses.dataclasses.Stroke
import baseclasses.dataclasses.StrokeInteractor
import java.io.IOException
import java.io.OutputStreamWriter

/*

    Считывание касания.

 */

class StrokeInputView(
    context: Context?,
    attrs: AttributeSet? = null,
    drawStrokeView: DrawStrokeView
) :
    View(context, attrs) {

    // public <-> ML
    val inputHandler = InputHandler(context, attrs, drawStrokeView)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val point = Point(event.x.toInt(), event.y.toInt(), inputHandler.currentTime++)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                inputHandler.touchStart(point)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                inputHandler.touchMove(point)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                inputHandler.touchUp(point)
                invalidate()
            }
        }
        return true
    }

}

class InputHandler(
    private val context: Context?,
    private val attrs: AttributeSet? = null,
    val drawStrokeView: DrawStrokeView
) {

    private var drawStrokeInteractor = DrawStrokeInteractor()

    var strokes: MutableList<Stroke> = ArrayList()

    var currentTime: Long = 0

    lateinit var lastPoint: Point

    fun clear() {
        currentTime = 0
        strokes.clear()
    }

    fun loadStrokes(path: String) {
        val outputData = ""
        strokes.forEach {
            it.points.forEach {
                outputData.plus(" " + it.x.toString() + "," + it.y.toString() + "," + it.time.toString())
            }
            outputData.plus("\n")
        }
        try {
            val outputStreamWriter = OutputStreamWriter(
                context!!.openFileOutput(
                    path,
                    Context.MODE_APPEND
                )
            )
            outputStreamWriter.write(outputData)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: " + e.toString())
        }
    }

    private var calls = 0

    private fun modifyLastStroke(point: Point) {
        calls++
        val pointInteractor = PointInteractor()
        val interactor = StrokeInteractor()
        pointInteractor.getPointsAroundLine(lastPoint, point, 3f).forEach {
            interactor.addPoint(strokes.last(), it)
            Log.println(Log.DEBUG, "dbg", calls.toString() + ": " + it.toString())
        }
        drawStrokeInteractor.set(drawStrokeView, strokes.last())
    }

    fun touchMove(point: Point) {
        modifyLastStroke(point)
        lastPoint = point
    }

    fun touchUp(point: Point) {
        modifyLastStroke(point)
    }

    fun touchStart(point: Point) {
        strokes.add(Stroke())
        lastPoint = point
        modifyLastStroke(point)
    }


}