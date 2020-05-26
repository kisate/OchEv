package com.example.ochev.viewclasses.drawers

import android.graphics.Color
import android.graphics.Paint

class EditingArrowheadDrawer: ArrowheadDrawer() {
    init {
        style.fillPaint.style = Paint.Style.FILL_AND_STROKE
        style.fillPaint.strokeWidth = 4f
        style.fillPaint.color = Color.parseColor("#FFC107")
    }
}
