package com.example.ochev.viewclasses.eventhandlers

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.editors.edgeeditor.EdgeEditor
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.SmartEditText
import com.example.ochev.viewclasses.buttonshandler.ButtonsHandler
import com.example.ochev.viewclasses.drawers.GraphDrawer
import com.example.ochev.viewclasses.drawers.LinesDrawer
import com.example.ochev.viewclasses.drawers.drawinginformations.DrawingMode
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
    private val linesDrawer: LinesDrawer,
    private val buttonsHandler: ButtonsHandler,
    private val editText: SmartEditText,
    private val classifier: Classifier
) {

    private var gestureEventHandler: GestureEventHandler? = null

    private var currentFigureEditor: VertexFigureEditor? = null

    private var currentEdgeEditor: EdgeEditor? = null

    fun handle(gesture: Gesture, event: MotionEvent) {

        if (gestureEventHandler == null) {
            gestureEventHandler = chooseHandler(gesture, event)
        }

        if (gestureEventHandler != null) gestureEventHandler!!.handle(gesture, event)

        if (gesture.state == GestureState.END) gestureEventHandler = null

        Log.d("Gestures", gesture.toString())
    }

    private fun chooseHandler(gesture: Gesture, event: MotionEvent): GestureEventHandler? {

        if (gesture.type != GestureType.NONE) exitEditTextMode()

        if (gesture.type == GestureType.SCROLL_AND_ZOOM) return ScrollAndZoomEventHandler(
            strokeDrawer,
            graphDrawer,
            classifier
        )

        val clickedFigureEditor =
            graphDrawer.graphEditor.getFigureEditorByTouch(Point(event))

        if (currentEdgeEditor != null && graphDrawer.graphEditor.getEdgeNodeByIdOrNull(currentEdgeEditor!!.figureId) != null && gesture.type == GestureType.TAP)
        {
            val clickedEnd = graphDrawer.graphEditor.getEdgeNodeByIdOrNull(currentEdgeEditor!!.figureId)!!.figure.getIndexOfClosestEnd(Point(event))
            if (clickedEnd != -1) {
                graphDrawer.graphEditor.getEdgeNodeByIdOrNull(currentEdgeEditor!!.figureId)!!.drawingInformation.switchTypeToNext(clickedEnd)
                graphDrawer.invalidate()
                Log.d("GEG", clickedEnd.toString())
                return null
            }
        }


        Log.d("Gestures", clickedFigureEditor?.figureId.toString())

        if (clickedFigureEditor is VertexFigureEditor?) return handleVertexClick(gesture, event, clickedFigureEditor)
        else if (clickedFigureEditor is EdgeEditor?)
        {
            handleEdgeClick(gesture, event, clickedFigureEditor)
        }

        return null
    }

    private fun handleEdgeClick(
        gesture: Gesture,
        event: MotionEvent,
        clickedEdgeEditor: EdgeEditor?
    ): GestureEventHandler? {

        if (gesture.type == GestureType.TAP) {

            if(clickedEdgeEditor == null) {
                exitAll()
                return null
            }

            enterEditEdgeMode(clickedEdgeEditor)

            return null
        }

        return null
    }

    private fun handleVertexClick(
        gesture: Gesture,
        event: MotionEvent,
        clickedFigureEditor: VertexFigureEditor?
    ): GestureEventHandler? {
        if (gesture.type == GestureType.TAP) {
            if (clickedFigureEditor == null) {
                exitAll()
                return null
            }

            enterEditMode(clickedFigureEditor)

            return null
        }

        if (gesture.type == GestureType.MOVE) {
            if (clickedFigureEditor == null) {
                exitAll()
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
                        linesDrawer,
                        classifier,
                        currentFigureEditor!!
                    )
                }
                clickedFigureEditor.figureId != currentFigureEditor!!.figureId -> {
                    exitAll()
                    return DrawingEventHandler(strokeDrawer, graphDrawer, classifier)
                }
            }
        }

        if (gesture.type == GestureType.LONG_TAP && clickedFigureEditor != null) {
            enterEditTextMode(clickedFigureEditor)
            if (gesture.state == GestureState.START) {
                val v =
                    graphDrawer.graphView.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v?.vibrate(
                        VibrationEffect.createOneShot(
                            100,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    v?.vibrate(100)
                }
            }
        }
        return null
    }

    private fun exitEditMode() {

        if (currentFigureEditor != null) {
            buttonsHandler.exitEditing()
            if (graphDrawer.graphEditor.getFigureNodeByIdOrNull(currentFigureEditor!!.figureId) != null) {
                graphDrawer.graphEditor.getFigureNodeByIdOrNull(currentFigureEditor!!.figureId)!!.drawingInformation.enterMode(
                    DrawingMode.DEFAULT
                )
            }
            currentFigureEditor = null
            graphDrawer.invalidate()
        }
    }

    private fun enterEditMode(clickedFigureEditor: VertexFigureEditor) {

        Log.d("GEG", "Entered edit")

        exitEditEdgeMode()
        exitEditMode()

        currentFigureEditor = clickedFigureEditor
        if (graphDrawer.graphEditor.getFigureNodeByIdOrNull(currentFigureEditor!!.figureId) != null) {
            graphDrawer.graphEditor.getFigureNodeByIdOrNull(currentFigureEditor!!.figureId)!!.drawingInformation.enterMode(
                DrawingMode.EDIT
            )

            graphDrawer.graphEditor.maximizeVertexHeightById(currentFigureEditor!!.figureId)
        }
        buttonsHandler.enterEditing(clickedFigureEditor)
        graphDrawer.invalidate()
    }

    private fun enterEditTextMode(clickedFigureEditor: VertexFigureEditor) {

        Log.d("GEG", "Entered text")

        editText.visibility = View.VISIBLE
        editText.setVertexEditor(clickedFigureEditor, graphDrawer.graphView)
        enterEditMode(clickedFigureEditor)

        buttonsHandler.disableAll()
    }

    private fun exitEditTextMode() {
        Log.d("GEG", "Left text")
        val view = (editText.context as Activity).currentFocus
        if (view != null) {
            val imm =
                (editText.context as Activity).getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(editText.windowToken, 0)
            view.clearFocus()
        }
        editText.visibility = View.GONE
        buttonsHandler.enableAll()
    }

    private fun enterEditEdgeMode(clickedEdgeEditor: EdgeEditor) {
        Log.d("GEG", "Entered edge")


        exitEditEdgeMode()
        exitEditMode()
        currentEdgeEditor = clickedEdgeEditor

        if (graphDrawer.graphEditor.getEdgeNodeByIdOrNull(clickedEdgeEditor.figureId) != null) {
            graphDrawer.graphEditor.getEdgeNodeByIdOrNull(clickedEdgeEditor.figureId) !!.drawingInformation.enterMode(
                DrawingMode.EDIT
            )
        }

        buttonsHandler.enterEditing(clickedEdgeEditor)

        graphDrawer.invalidate()
    }

    private fun exitEditEdgeMode() {

        Log.d("GEG", "Left edge")

        if (currentEdgeEditor != null)
        {
            buttonsHandler.exitEditing()

            if (graphDrawer.graphEditor.getEdgeNodeByIdOrNull(currentEdgeEditor!!.figureId) != null) {
                graphDrawer.graphEditor.getEdgeNodeByIdOrNull(currentEdgeEditor!!.figureId)!!.drawingInformation.enterMode(
                    DrawingMode.DEFAULT
                )
            }

            graphDrawer.invalidate()

            currentEdgeEditor = null

        }
    }

    private fun exitAll() {
        exitEditMode()
        exitEditEdgeMode()
        exitEditTextMode()
    }
}