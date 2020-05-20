package com.example.ochev.viewclasses.eventhandlers

import android.util.Log
import android.view.MotionEvent
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Vector
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.GraphDrawer
import com.example.ochev.viewclasses.StrokeDrawer
import com.example.ochev.viewclasses.buttonshandler.ButtonsHandler

class ScrollAndZoomEventHandler(
    strokeDrawer: StrokeDrawer,
    drawGraphView: GraphDrawer,
    classifier: Classifier
) : GestureEventHandler(strokeDrawer, drawGraphView, classifier) {

    private var lastCenter: Point? = null
    private var lastDistance: Float? = null

    override fun handle(gesture: Gesture, event: MotionEvent){

        when (gesture.state) {
            GestureState.START -> {
                lastCenter = Point(event)
                lastDistance = calcDistance(event)

            }
        }

        if (gesture.state == GestureState.START)
        {

        }

        if (event.pointerCount == 2)
        {
            Log.i("Scrolling", event.getPointerId(0).toString())
            Log.i("Scrolling", "${event.getX(0)} ${event.getY(0)}")
            Log.i("Scrolling", "${event.getX(1)} ${event.getY(1)}")
            Log.i("Scrolling", "${event.x} ${event.y}")
            Log.i("Scrolling", "${Vector(calcCenter(event), lastCenter!!)}")
        }
    }

    private fun calcCenter(event: MotionEvent): Point {

        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)

        return Point((x/2).toInt(), (y/2).toInt())
    }

    private fun calcDistance(event: MotionEvent): Float {

        return Point(event.getX(0).toInt(), event.getY(0).toInt()).getDistanceToPoint(Point(event.getX(1).toInt(), event.getY(1).toInt()))
    }
}