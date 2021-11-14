package com.example.ochev.ui

import android.view.MotionEvent
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke

class InputStrokeHandler(
    private val inputDrawView: InputDrawView?
) {
    var stroke: Stroke = Stroke()
        private set

    fun onStart(event: MotionEvent) {
        inputDrawView?.clear()
        stroke = Stroke()
        stroke.addPoint(Point(event))
        inputDrawView?.onStart(event)
    }

    fun onContinue(event: MotionEvent) {
        stroke.addPoint(Point(event))
        inputDrawView?.onEvent(event)
    }
}