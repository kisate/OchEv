package com.example.ochev.viewclasses.eventhandlers

import android.util.Log
import android.view.MotionEvent
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.GraphDrawer
import com.example.ochev.viewclasses.StrokeDrawer

class ShapingEventHandler(
    strokeDrawer: StrokeDrawer,
    private val drawGraphView: GraphDrawer,
    classifier: Classifier,
    private val vertexFigureEditor: VertexFigureEditor
) : GestureEventHandler(strokeDrawer, drawGraphView, classifier) {

    override fun handle(gesture: Gesture, event: MotionEvent) {
        val point = event.let{ Point(it) }
        when (gesture.state) {
            GestureState.START -> {
                vertexFigureEditor.shaper.shapingBegins(point)
            }
            GestureState.IN_PROGRESS -> {
                vertexFigureEditor.shaper.nextPoint(point)
            }
            GestureState.END -> {
                vertexFigureEditor.shaper.nextPoint(point)
            }
            else -> {

            }
        }
        drawGraphView.graphView.invalidate()
    }
}