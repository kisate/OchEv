package com.example.ochev.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class InputDrawView(
    context: Context,
    attributeSet: AttributeSet
) : View(context, attributeSet) {
    private var path: Path = Path()
    var paint: Paint = Paint()
    private var isActual = true

    fun clear() {
        path = Path()
        isActual = false
    }

    fun onStart(event: MotionEvent) {
        Log.d(TAG, "on start $event")
        path.moveTo(event.x, event.y)
    }

    fun onEvent(event: MotionEvent) {
        Log.d(TAG, "on event $event")
        path.lineTo(event.x, event.y)
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawPath(path, paint)
    }

    companion object {
        private val TAG = "InputDrawView"
    }
}