package com.example.ochev.viewclasses.eventhandlers

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.DrawingMode
import com.example.ochev.viewclasses.SmartEditText
import com.example.ochev.viewclasses.graphdrawers.GraphDrawer
import com.example.ochev.viewclasses.buttonshandler.ButtonsHandler
import com.example.ochev.viewclasses.strokedrawers.StrokeDrawer

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
    private val editText: SmartEditText,
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

        if(gesture.type != GestureType.NONE) exitEditTextMode()

        if (gesture.type == GestureType.SCROLL_AND_ZOOM) return ScrollAndZoomEventHandler(
            strokeDrawer,
            graphDrawer,
            classifier
        )

        val clickedFigureEditor =
            graphDrawer.graphView.graphEditor.getFigureEditorByTouch(Point(event))

        Log.d("Gestures", clickedFigureEditor?.figureId.toString())

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
                return DrawingEventHandler(strokeDrawer, graphDrawer, classifier)
            }

            when {
                currentFigureEditor!!.shaper.shapingBegins(Point(event)) -> {
                    return ShapingEventHandler(
                        strokeDrawer,
                        graphDrawer,
                        classifier,
                        currentFigureEditor!!
                    )
                }
                currentFigureEditor!!.mover.moveBegins(Point(event)) -> {
                    return MovingEventFigureHandler(
                        strokeDrawer,
                        graphDrawer,
                        classifier,
                        currentFigureEditor!!
                    )
                }
                clickedFigureEditor.figureId != currentFigureEditor!!.figureId -> {
                    exitEditMode()
                    return DrawingEventHandler(strokeDrawer, graphDrawer, classifier)
                }
            }
        }

        if (gesture.type == GestureType.LONG_TAP && clickedFigureEditor != null)
        {
            enterEditTextMode(clickedFigureEditor)
        }

        return null
    }

    private fun exitEditMode() {
        if (currentFigureEditor != null) {
            buttonsHandler.closeEditing()
            if (graphDrawer.graphView.graphEditor.getFigureNodeByIdOrNull(currentFigureEditor!!.figureId) != null) {
                graphDrawer.graphView.graphEditor.getFigureNodeByIdOrNull(currentFigureEditor!!.figureId)!!.drawingInformation.drawingMode = DrawingMode.DEFAULT
            }
            currentFigureEditor = null
            graphDrawer.graphView.invalidate()
        }
    }

    private fun enterEditMode(clickedFigureEditor: VertexFigureEditor) {
        exitEditMode()
        currentFigureEditor = clickedFigureEditor
        if (graphDrawer.graphView.graphEditor.getFigureNodeByIdOrNull(currentFigureEditor!!.figureId) != null) {
            graphDrawer.graphView.graphEditor.getFigureNodeByIdOrNull(currentFigureEditor!!.figureId)!!.drawingInformation.drawingMode = DrawingMode.EDIT

            graphDrawer.graphView.graphEditor.maximazeVertexHeightById(currentFigureEditor!!.figureId)
        }
        buttonsHandler.enterEditing(clickedFigureEditor)
        graphDrawer.graphView.invalidate()
        Log.d("Gestures", "Entered")
    }

    private fun enterEditTextMode(clickedFigureEditor: VertexFigureEditor) {
        editText.visibility = View.VISIBLE
        editText.setVertexEditor(clickedFigureEditor)
    }

    private fun exitEditTextMode()
    {
        val view = (editText.context as Activity).currentFocus
        if (view != null)
        {
            val imm = (editText.context as Activity).getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(editText.windowToken, 0)
            view.clearFocus()
        }
        editText.visibility = View.GONE
    }
}