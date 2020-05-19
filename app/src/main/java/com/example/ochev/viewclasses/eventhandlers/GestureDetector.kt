package com.example.ochev.viewclasses.eventhandlers


import android.view.MotionEvent
import com.example.ochev.baseclasses.dataclasses.Point

enum class GestureType() {
    TAP,
    MOVE,
    SCROLL;
}


class GestureDetector() {

    private var canBeTap = true
    private var firstPoint: Point? = null
    private var currentType: GestureType? = null
    private var pointerCount = 0

    fun detect(event: MotionEvent): GestureType? {
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
        return currentType
    }

    private fun gestureStart(event: MotionEvent) {
        canBeTap = true
        firstPoint = Point(event)
        currentType = null
        pointerCount = 0
    }

    private fun onTouchDown(event: MotionEvent): GestureType? {

        pointerCount += 1

        if (firstPoint == null) {
            gestureStart(event)
        }
        if (checkScrollingStart(event)) {
            currentType = GestureType.SCROLL
            return GestureType.SCROLL
        }

        return null
    }

    private fun onTouchMove(event: MotionEvent): GestureType? {

        if (currentType != null) return currentType

        if (checkScrollingStart(event))
        {
            currentType = GestureType.SCROLL
            return GestureType.SCROLL
        }

        if (!checkCanBeTap(event))
        {
            canBeTap = false
            currentType = GestureType.MOVE
            return GestureType.MOVE
        }

        return null
    }

    private fun onTouchUp(event: MotionEvent): GestureType? {

        pointerCount -= 1

        val lastGestureType = currentType

        if (pointerCount == 0) gestureEnd(event)

        if (currentType != null) return lastGestureType
        if (canBeTap && checkCanBeTap(event)) return GestureType.TAP

        return null
    }

    private fun checkCanBeTap(event: MotionEvent): Boolean {
        return Point(event).getDistanceToPoint(firstPoint!!) <= TAP_THRESHOLD
    }

    private fun checkScrollingStart(event: MotionEvent): Boolean {
        return event.pointerCount == 2
    }

    private fun gestureEnd(event: MotionEvent) {
        firstPoint = null
    }

    companion object {
        private const val TAP_THRESHOLD = 50f
    }
}