package com.example.ochev.viewclasses.eventhandlers

import android.view.MotionEvent
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.DrawGraphView
import com.example.ochev.viewclasses.DrawStrokeView

class DrawingEventHandler(
    drawStrokeView: DrawStrokeView,
    drawGraphView: DrawGraphView,
    classifier: Classifier
) : GestureEventHandler(drawStrokeView, drawGraphView, classifier) {

    override fun handle(gesture: Gesture, event: MotionEvent) {
//        TODO("Not yet implemented")
    }
}