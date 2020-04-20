package com.example.ochev.viewclasses

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter

class StrokeInputView(
    context: Context?,
    attrs: AttributeSet? = null,
    drawStrokeView: DrawStrokeView
) :
    View(context, attrs) {

    // public <-> ML
    val inputHandler = InputHandler(context, drawStrokeView)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val point = Point(event.x.toInt(), event.y.toInt())
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
    private val drawStrokeView: DrawStrokeView
) {

    private var drawStrokeInteractor = DrawStrokeInteractor()

    private var strokes: MutableList<Stroke> = ArrayList()

    private lateinit var lastPoint: Point

    fun clear() {
        strokes.clear()
    }

    fun loadStrokes(path: String) {
        var outputData = ""
        Log.println(Log.DEBUG, "dbgFile", strokes.toString())
        var cnt = 0
        strokes.forEach {
            it.points.forEach {
                //Log.println(Log.DEBUG, "dbgFileString", it.toString())
                outputData += " " + it.x.toString() + "," + it.y.toString()
                cnt++
            }
            outputData += "\n"
        }
        Log.println(Log.DEBUG, "dbgFileString", outputData)
        Log.println(Log.DEBUG, "dbgCount", cnt.toString())
        try {
            val file = File(context!!.getExternalFilesDir(null), path)
            val fileOutput = FileOutputStream(file)
            val outputStreamWriter = OutputStreamWriter(fileOutput)
            outputStreamWriter.write(outputData)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: " + e.toString())
        }
    }

    private fun modifyLastStroke(point: Point) {
        strokes.last().addPoint(point)
        drawStrokeInteractor.add(drawStrokeView, strokes.last())
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
        drawStrokeView.path.moveTo(point.x.toFloat(), point.y.toFloat())
        modifyLastStroke(point)
    }


}