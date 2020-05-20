package com.example.ochev.viewclasses.eventhandlers

import android.util.Log
import android.view.MotionEvent
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.DrawGraphView
import com.example.ochev.viewclasses.DrawStrokeView
import com.example.ochev.viewclasses.StrokeDrawer

abstract class GestureEventHandler(
    private val strokeDrawer: StrokeDrawer,
    private val drawGraphView: DrawGraphView,
    private val classifier: Classifier
) {
    abstract fun handle(gesture: Gesture, event: MotionEvent)
}

class GestureHandler(
    private val strokeDrawer: StrokeDrawer,
    private val drawGraphView: DrawGraphView,
    private val classifier: Classifier
) {

    private var gestureEventHandler: GestureEventHandler? = null

    private var currentFigureEditor: VertexFigureEditor? = null

    fun handle(gesture: Gesture, event: MotionEvent) {

        if (gestureEventHandler == null) {
            gestureEventHandler = chooseHandler(gesture, event)
        }

        if (gestureEventHandler != null) gestureEventHandler!!.handle(gesture, event)

        if (gesture.state == GestureState.END) gestureEventHandler = null

        Log.d("Gestures", gesture.toString())
    }

    private fun chooseHandler(gesture: Gesture, event: MotionEvent): GestureEventHandler? {

        if (gesture.type == GestureType.SCROLL) return ScrollingEventHandler(
            strokeDrawer,
            drawGraphView,
            classifier
        )

        currentFigureEditor = drawGraphView.graphEditor.getFigureEditorByTouch(Point(event))

        if (currentFigureEditor == null) {
            if (gesture.type == GestureType.MOVE) return DrawingEventHandler(
                strokeDrawer,
                drawGraphView,
                classifier
            )
        } else {
            return EditingEventHandler(
                strokeDrawer, drawGraphView, classifier,
                currentFigureEditor!!
            )
        }

        return null
    }
}