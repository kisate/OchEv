package com.example.ochev.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class InputView(
    context: Context,
    attributeSet: AttributeSet
) : View(context, attributeSet) {

    private var listener: OnTouchEventListener? = null

    fun setOnTouchEventListener(listener: OnTouchEventListener) {
        this.listener = listener
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d("kirill", "$event")
        return listener?.onTouchEvent(event) ?: false
    }


}