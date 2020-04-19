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
import baseclasses.dataclasses.Stroke
import baseclasses.dataclasses.StrokeInteractor
import java.io.IOException
import java.io.OutputStreamWriter

/*

    Считывание касания.

 */


class StrokeInput(
    context: Context?,
    attrs: AttributeSet? = null
) :
    View(context, attrs) {

    var strokes: MutableList<Stroke> = ArrayList()

    private var currentTime: Long = 0

    fun loadStrokes(path: String) {
        var outputData = ""
        var pointCnt = 0
        strokes.forEach {
            it.points.forEach {
                Log.println(
                    DEBUG,
                    "dbg",
                    it.x.toString() + " " + it.y.toString() + " " + it.time.toString()
                )
                outputData += " " + it.x.toString() + " " + it.y.toString() + " " + it.time.toString()
                pointCnt++
            }
        }
        outputData = pointCnt.toString() + outputData
        currentTime = 0
        strokes.clear()
        try {
            val outputStreamWriter = OutputStreamWriter(
                context.openFileOutput(
                    path,
                    Context.MODE_PRIVATE
                )
            )
            outputStreamWriter.write(data)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: " + e.toString())
        }
    }

    private fun getPointsAround(x: Float, y: Float, width: Int = 1): MutableList<Point> {
        val points: MutableList<Point> = ArrayList()
        for (newX in x.toInt() - width..x.toInt() + width) {
            for (newY in y.toInt() - width..y.toInt() + width) {
                if ((newX - x) * (newX - x) + (newY - y) * (newY - y) <= width * width) {
                    Log.println(DEBUG, "dbg", newX.toString() + " " + newY.toString())
                    points.add(Point(newX, newY, currentTime))
                }
            }
        }
        return points
    }

    private fun modifyLastStroke(x: Float, y: Float) {
        val points = getPointsAround(x, y)
        val interactor = StrokeInteractor()
        points.forEach {
            interactor.addPoint(strokes.last(), it)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        currentTime++
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp(x, y)
                invalidate()
            }
        }
        return true
    }

    private fun touchMove(x: Float, y: Float) {
        Log.println(DEBUG, "dbg", "touching! x: " + x.toString() + " y: " + y.toString())
        modifyLastStroke(x, y)
    }

    private fun touchUp(x: Float, y: Float) {
        Log.println(DEBUG, "dbg", "touch ended! x: " + x.toString() + " y: " + y.toString())
        modifyLastStroke(x, y)
    }

    private fun touchStart(x: Float, y: Float) {
        Log.println(DEBUG, "dbg", "touch started! x: " + x.toString() + " y: " + y.toString())
        strokes.add(Stroke())
        modifyLastStroke(x, y)
    }

}