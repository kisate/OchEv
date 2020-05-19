package com.example.ochev.viewclasses.eventhandlers


import android.util.Log
import android.view.MotionEvent
import com.example.ochev.baseclasses.dataclasses.Point


class GestureDetector {

    private var canBeTap = true
    private var firstPoint: Point? = null
    private var currentGesture = Gesture()
    private var pointerCount = 0

    fun detect(event: MotionEvent): Gesture {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return onTouchDown(event)
            }
            MotionEvent.ACTION_MOVE -> {
                return onTouchMove(event)
            }
            MotionEvent.ACTION_UP -> {
                return onTouchUp(event)
            }
        }
        return currentGesture
    }

    private fun gestureStart(event: MotionEvent) {
        canBeTap = true
        firstPoint = Point(event)
        currentGesture = Gesture()
        pointerCount = 0
    }

    private fun onTouchDown(event: MotionEvent): Gesture {

        if (firstPoint == null) {
            gestureStart(event)
        }

        pointerCount += 1

        if (checkScrollingStart(event)) {
            currentGesture = Gesture(GestureType.SCROLL, currentGesture.state)
            return currentGesture
        }

        return Gesture()
    }

    private fun onTouchMove(event: MotionEvent): Gesture {

        if (currentGesture.type != GestureType.NONE) return Gesture(currentGesture.type, GestureState.IN_PROGRESS)

        if (checkScrollingStart(event))
        {
            currentGesture = Gesture(GestureType.SCROLL, GestureState.START)
            return currentGesture
        }

        if (!checkCanBeTap(event))
        {
            canBeTap = false
            currentGesture = Gesture(GestureType.MOVE, GestureState.START)
            return currentGesture
        }

        return Gesture()
    }

    private fun onTouchUp(event: MotionEvent): Gesture {

        pointerCount -= 1

        val lastGesture = currentGesture.copy()

        if (pointerCount == 0) gestureEnd(event)

        if (lastGesture.type != GestureType.NONE) return Gesture(lastGesture.type, GestureState.END)
        if (canBeTap && checkCanBeTap(event)) return Gesture(GestureType.TAP, GestureState.END)

        return Gesture()
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