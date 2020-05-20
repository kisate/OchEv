package com.example.ochev.viewclasses.eventhandlers

import android.view.MotionEvent
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.GraphDrawer
import com.example.ochev.viewclasses.StrokeDrawer
import com.example.ochev.viewclasses.buttonshandler.ButtonsHandler

abstract class GestureEventHandler(
    private val strokeDrawer: StrokeDrawer,
    private val drawGraphView: GraphDrawer,
    private val classifier: Classifier
) {
    abstract fun handle(gesture: Gesture, event: MotionEvent)
}

class GestureHandler(
    private val strokeDrawer: StrokeDrawer,
    private val graphDrawer: GraphDrawer,
    private val buttonsHandler: ButtonsHandler,
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
    }

    private fun chooseHandler(gesture: Gesture, event: MotionEvent): GestureEventHandler? {

        if (gesture.type == GestureType.SCROLL_AND_ZOOM) return ScrollAndZoomEventHandler(
            strokeDrawer,
            graphDrawer,
            classifier
        )

        val clickedFigureEditor = graphDrawer.graphView.graphEditor.getFigureEditorByTouch(Point(event))

        if (gesture.type == GestureType.TAP)
        {
            if (clickedFigureEditor == null)
            {
                exitEditMode()
                return null
            }
            enterEditMode(clickedFigureEditor)
            return null
        }

        if (gesture.type == GestureType.MOVE)
        {
            if (clickedFigureEditor == null)
            {
                exitEditMode()
                return DrawingEventHandler(strokeDrawer, graphDrawer, classifier)
            }
            if (currentFigureEditor == null)
            {
                return ConnectingEventHandler(strokeDrawer, graphDrawer, classifier)
            }
            if (clickedFigureEditor.mover.moveBegins(Point(event)))
            {
                return MovingFigureHandler(strokeDrawer, graphDrawer, classifier)
            }
            else if (clickedFigureEditor.shaper.shapingBegins(Point(event)))
            {
                return ShapingEventHandler(strokeDrawer, graphDrawer, classifier)
            }
        }

        return null
    }

    private fun exitEditMode() {
        if (currentFigureEditor != null)
        {
            currentFigureEditor = null
            buttonsHandler.closeEditing()
        }
    }

    private fun enterEditMode(clickedFigureEditor: VertexFigureEditor) {
        currentFigureEditor = clickedFigureEditor
        buttonsHandler.enterEditing(clickedFigureEditor)
    }
}