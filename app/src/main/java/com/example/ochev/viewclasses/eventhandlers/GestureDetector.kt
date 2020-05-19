package com.example.ochev.viewclasses.eventhandlers


import android.util.Log
import android.view.MotionEvent
import com.example.ochev.baseclasses.dataclasses.Point


class GestureDetector {

    private var canBeTap = true
    private var firstPoint: Point? = null
    private var currentGesture = Gesture()

    fun detect(event: MotionEvent): Gesture {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onTouchDown(event)
            }
            MotionEvent.ACTION_MOVE -> {
                onTouchMove(event)
            }
            MotionEvent.ACTION_UP -> {
                onTouchUp(event)
            }
            else -> {
                currentGesture
            }
        }
    }

    private fun gestureStart(event: MotionEvent) {
        canBeTap = true
        firstPoint = Point(event)
        currentGesture = Gesture()
    }

    private fun onTouchDown(event: MotionEvent): Gesture {

        if (firstPoint == null) {
            gestureStart(event)
        }


        if (checkScrollingStart(event)) {
            currentGesture = Gesture(GestureType.SCROLL, GestureState.START)
            return currentGesture
        }

        return Gesture()
    }

    private fun onTouchMove(event: MotionEvent): Gesture {

        if (currentGesture.type != GestureType.NONE) {

            currentGesture = Gesture(currentGesture.type, GestureState.IN_PROGRESS)

            return currentGesture
        }

        if (checkScrollingStart(event)) {
            currentGesture = Gesture(GestureType.SCROLL, GestureState.START)
            return currentGesture
        }

        if (!checkCanBeTap(event)) {
            canBeTap = false
            currentGesture = Gesture(GestureType.MOVE, GestureState.START)
            return currentGesture
        }

        return Gesture()
    }

    private fun onTouchUp(event: MotionEvent): Gesture {

        var gesture = currentGesture.copy()


        gesture = if (gesture.type != GestureType.NONE) Gesture(gesture.type, GestureState.END)
        else if (canBeTap && checkCanBeTap(event)) Gesture(GestureType.TAP, GestureState.END)
        else Gesture()

        gestureEnd(event)

        return gesture
    }

    private fun checkCanBeTap(event: MotionEvent): Boolean {
        return Point(event).getDistanceToPoint(firstPoint!!) <= TAP_THRESHOLD
    }

    private fun checkScrollingStart(event: MotionEvent): Boolean {
        return event.pointerCount == 2
    }

    private fun gestureEnd(event: MotionEvent) {
        firstPoint = null
        currentGesture = Gesture()
    }

    companion object {
        private const val TAP_THRESHOLD = 50f
    }
}