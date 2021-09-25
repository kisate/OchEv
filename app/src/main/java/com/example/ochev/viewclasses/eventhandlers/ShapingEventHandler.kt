package com.example.ochev.viewclasses.eventhandlers

import android.view.MotionEvent
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.drawers.GraphDrawer
import com.example.ochev.viewclasses.strokedrawers.StrokeDrawer

class ShapingEventHandler(
    strokeDrawer: StrokeDrawer,
    private val graphDrawer: GraphDrawer,
    classifier: Classifier,
    private val vertexFigureEditor: VertexFigureEditor
) : GestureEventHandler(strokeDrawer, graphDrawer, classifier) {

    private var firstPointerId: Int? = null

    override fun handle(gesture: Gesture, event: MotionEvent) {
        if (firstPointerId != null && event.getPointerId(0) != firstPointerId) return
        val point = event.let { Point(it) }
        when (gesture.state) {
            GestureState.START -> {
                firstPointerId = event.getPointerId(0)
            }
            GestureState.IN_PROGRESS -> {
                vertexFigureEditor.shaper.nextPoint(point)
            }
            GestureState.END -> {
                firstPointerId = null
                vertexFigureEditor.shaper.nextPoint(point)
            }
            else -> {

            }
        }
        graphDrawer.invalidate()
    }
}