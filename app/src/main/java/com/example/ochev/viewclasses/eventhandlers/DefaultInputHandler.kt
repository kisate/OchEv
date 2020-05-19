package com.example.ochev.viewclasses.eventhandlers

import android.view.MotionEvent
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.DrawGraphView
import com.example.ochev.viewclasses.DrawStrokeView

abstract class InputEventHandler(
    private val drawStrokeView: DrawStrokeView,
    private val drawGraphView: DrawGraphView,
    private val classifier: Classifier
) {
    abstract fun handle(event: MotionEvent) : Boolean
}

class DefaultInputHandler(
    private val drawStrokeView: DrawStrokeView,
    private val drawGraphView: DrawGraphView,
    private val classifier: Classifier
) {

    private lateinit var firstPoint: Point
    private lateinit var lastPoint: Point
    private var canBeTap = true
    private var actionInProgress = false

    private var currentEventHandler: InputEventHandler? = null

    private var vertexFigureEditor: VertexFigureEditor? = null

    private val gestureDetector = GestureDetector()

    fun handle(event: MotionEvent) {


        if (currentEventHandler != null)
        {
            if (currentEventHandler!!.handle(event))
            {
                finishAction(event)
            }
        }
        else
        {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    touchDown(event)
                }
                MotionEvent.ACTION_MOVE -> {
                    touchMove(event)
                }
                MotionEvent.ACTION_UP -> {
                    touchUp(event)
                }
            }
        }
    }

    private fun startAction(event: MotionEvent) {
        firstPoint = Point(event)
        lastPoint = Point(event)
        canBeTap = true
        currentEventHandler = null

        actionInProgress = true

    }

    private fun touchDown(event: MotionEvent) {
        if (!actionInProgress) startAction(event)
        if (event.pointerCount == 2) {
            currentEventHandler = ScrollingEventHandler(drawStrokeView, drawGraphView, classifier)
            return
        }
    }

    private fun touchMove(event: MotionEvent) {
        if (event.pointerCount == 2) {
            currentEventHandler = ScrollingEventHandler(drawStrokeView, drawGraphView, classifier)
            return
        }
        if (firstPoint.getDistanceToPoint(lastPoint) > TAP_THRESHOLD) canBeTap = false
    }

    private fun touchUp(event: MotionEvent) {
        if (canBeTap) {

            if (vertexFigureEditor != null)
            {
                if ()
            }

            val vertexFigureEditor = drawGraphView.graphEditor.getFigureEditorByTouch(Point(event))

            currentEventHandler = vertexFigureEditor?.let {
                EditingEventHandler(drawStrokeView, drawGraphView, classifier,
                    it
                )
            }
            return
        }
    }

    private fun finishAction(event: MotionEvent) {

        actionInProgress = false
        currentGestureType = null
    }

    companion object {
        private const val TAP_THRESHOLD = 50f
    }
}