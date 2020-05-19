package com.example.ochev.viewclasses.eventhandlers

import android.view.MotionEvent
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.DrawGraphView
import com.example.ochev.viewclasses.DrawStrokeView

abstract class GestureEventHandler(
    private val drawStrokeView: DrawStrokeView,
    private val drawGraphView: DrawGraphView,
    private val classifier: Classifier
) {
    abstract fun handle(gestureType: GestureType?, event: MotionEvent): GestureType?
}

class GestureHandler(
    private val drawStrokeView: DrawStrokeView,
    private val drawGraphView: DrawGraphView,
    private val classifier: Classifier
) {

    private var gestureEventHandler: GestureEventHandler? = null

    private var currentFigureEditor: VertexFigureEditor? = null

    fun handle(gestureType: GestureType?, event: MotionEvent): GestureType? {
        if (gestureType == null) return null

        if (gestureEventHandler == null) {
            gestureEventHandler = chooseHandler(gestureType, event)
        }

        if (gestureEventHandler != null) return gestureEventHandler!!.handle(gestureType, event)

        return null
    }

    private fun chooseHandler(gestureType: GestureType?, event: MotionEvent): GestureEventHandler? {

        if (gestureType == GestureType.SCROLL) return ScrollingEventHandler(
            drawStrokeView,
            drawGraphView,
            classifier
        )

        currentFigureEditor = drawGraphView.graphEditor.getFigureEditorByTouch(Point(event))

        if (currentFigureEditor == null) {
            if (gestureType == GestureType.MOVE) return DrawingEventHandler(
                drawStrokeView,
                drawGraphView,
                classifier
            )
        } else {
            return EditingEventHandler(
                drawStrokeView, drawGraphView, classifier,
                currentFigureEditor!!
            )
        }

        return null
    }

}