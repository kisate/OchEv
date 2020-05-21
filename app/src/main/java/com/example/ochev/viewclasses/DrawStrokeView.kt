package com.example.ochev.viewclasses

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.viewclasses.strokedrawers.PathDrawer

class DrawStrokeView(
    context: Context?,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    val path = Path()
    val pathDrawer = PathDrawer()

    override fun onDraw(canvas: Canvas?) {
        pathDrawer.draw(path, canvas)
    }
}



