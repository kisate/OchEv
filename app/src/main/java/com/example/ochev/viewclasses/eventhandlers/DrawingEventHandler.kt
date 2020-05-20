package com.example.ochev.viewclasses.eventhandlers

import android.view.MotionEvent
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.DrawGraphView
import com.example.ochev.viewclasses.DrawStrokeView
import com.example.ochev.viewclasses.StrokeDrawer

class DrawingEventHandler(
    private val strokeDrawer: StrokeDrawer,
    drawGraphView: DrawGraphView,
    classifier: Classifier
) : GestureEventHandler(strokeDrawer, drawGraphView, classifier) {

    override fun handle(gesture: Gesture, event: MotionEvent) {
        when (gesture.state) {
            GestureState.START -> {
                strokeDrawer.add(event.x, event.y)
            }
            GestureState.IN_PROGRESS -> {
                strokeDrawer.add(event.x, event.y)
            }
            GestureState.END -> {
                strokeDrawer.add(event.x, event.y)
                strokeDrawer.clear()
            }
            else -> {

            }
        }

    }
}