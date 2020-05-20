package com.example.ochev.viewclasses.eventhandlers

import android.util.Log
import android.view.MotionEvent
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.GraphDrawer
import com.example.ochev.viewclasses.StrokeDrawer

class MovingEventFigureHandler(
    strokeDrawer: StrokeDrawer,
    drawGraphView: GraphDrawer,
    classifier: Classifier
) : GestureEventHandler(strokeDrawer, drawGraphView, classifier) {

    override fun handle(gesture: Gesture, event: MotionEvent) {



    }

}