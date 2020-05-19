package com.example.ochev.viewclasses.eventhandlers

import android.util.Log
import android.view.MotionEvent
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.DrawGraphView
import com.example.ochev.viewclasses.DrawStrokeView

class ScrollingEventHandler(
    drawStrokeView: DrawStrokeView,
    drawGraphView: DrawGraphView,
    classifier: Classifier
) : GestureEventHandler(drawStrokeView, drawGraphView, classifier) {

    private var lastCenter: Point? = null

    override fun handle(gesture: Gesture, event: MotionEvent){
        if (event.pointerCount == 2)
        {
            Log.i("Scrolling", event.getPointerId(0).toString())
            Log.i("Scrolling", "${event.getX(0)} ${event.getY(0)}")
            Log.i("Scrolling", "${event.getX(1)} ${event.getY(1)}")
            Log.i("Scrolling", "${event.x} ${event.y}")
        }
    }
}