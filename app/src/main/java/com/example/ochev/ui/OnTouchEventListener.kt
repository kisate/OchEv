package com.example.ochev.ui

import android.view.MotionEvent

fun interface OnTouchEventListener {
    fun onTouchEvent(event: MotionEvent): Boolean
}