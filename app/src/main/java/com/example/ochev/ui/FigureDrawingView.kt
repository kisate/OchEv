package com.example.ochev.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rectangle

class FigureDrawingView(
    context: Context,
    attributeSet: AttributeSet
) : View(context, attributeSet) {
    var figures: List<Figure> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    var paint: Paint = Paint()

    override fun onDraw(canvas: Canvas?) {
        Log.e(TAG, "on draw with ${figures.size} figures")
        for (figure in figures) {
            if (figure is Rectangle) {
                val path = Path()
                val points =
                    listOf(figure.leftDownCorner, figure.rightDownCorner, figure.rightUpCorner)
                path.moveTo(points[0].x, points[1].x)
                for (i in 1..2) {
                    path.moveTo(points[i].x, points[i].y)
                }
                path.close()
                canvas?.drawPath(path, paint)
            }

        }
    }

    companion object {
        private val TAG = "FigureDrawingView"
    }

}