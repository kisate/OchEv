package com.example.ochev.viewclasses.eventhandlers

import android.util.Log
import android.view.MotionEvent
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.DrawingMode
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
            Log.d("Gestures", "AA")
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

        val clickedFigureEditor =
            graphDrawer.graphView.graphEditor.getFigureEditorByTouch(Point(event))

        Log.d("Gestures", clickedFigureEditor.toString())

        if (gesture.type == GestureType.TAP) {
            if (clickedFigureEditor == null) {
                exitEditMode()
                return null
            }

            enterEditMode(clickedFigureEditor)

            return null
        }

        if (gesture.type == GestureType.MOVE) {
            if (clickedFigureEditor == null) {
                exitEditMode()
                return DrawingEventHandler(strokeDrawer, graphDrawer, classifier)
            }
            if (currentFigureEditor == null) {
                return ConnectingEventHandler(strokeDrawer, graphDrawer, classifier)
            }
            // is it right???
            if (clickedFigureEditor.figureId != currentFigureEditor!!.figureId) return null

            if (clickedFigureEditor.shaper.shapingBegins(Point(event))) {
                return ShapingEventHandler(
                    strokeDrawer,
                    graphDrawer,
                    classifier,
                    clickedFigureEditor
                )
            } else if (clickedFigureEditor.mover.moveBegins(Point(event))) {
                return MovingEventFigureHandler(
                    strokeDrawer,
                    graphDrawer,
                    classifier,
                    clickedFigureEditor
                )
            }
        }

        return null
    }

    private fun exitEditMode() {
        if (currentFigureEditor != null) {
            buttonsHandler.closeEditing()
            if (graphDrawer.graphView.graphEditor.getFigureNodeByIdOrNull(currentFigureEditor!!.figureId) != null) {
                graphDrawer.graphView.graphEditor.getFigureNodeByIdOrNull(currentFigureEditor!!.figureId)!!.drawingInformation.drawingMode =
                    DrawingMode.DEFAULT
            }
            currentFigureEditor = null
            graphDrawer.graphView.invalidate()
        }
    }

    private fun enterEditMode(clickedFigureEditor: VertexFigureEditor) {
        exitEditMode()
        currentFigureEditor = clickedFigureEditor
        if (graphDrawer.graphView.graphEditor.getFigureNodeByIdOrNull(currentFigureEditor!!.figureId) != null) {
            graphDrawer.graphView.graphEditor.getFigureNodeByIdOrNull(currentFigureEditor!!.figureId)!!.drawingInformation.drawingMode =
                DrawingMode.EDIT
        }
        buttonsHandler.enterEditing(clickedFigureEditor)
        graphDrawer.graphView.invalidate()
        Log.d("Gestures", "Entered")
    }
}