package com.example.ochev.viewclasses.graphdrawers

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.edgefigures.Edge
import com.example.ochev.viewclasses.DrawingInformation
import com.example.ochev.viewclasses.DrawingMode

class EdgeDrawer : Drawer() {

    init {
        /*
            default style of lines
         */
        styles[DrawingMode.DEFAULT.ordinal].circuitPaint.style = Paint.Style.STROKE
        styles[DrawingMode.DEFAULT.ordinal].circuitPaint.color = Color.BLACK
        styles[DrawingMode.DEFAULT.ordinal].circuitPaint.strokeWidth = 10f
        styles[DrawingMode.DEFAULT.ordinal].fontPaint.style = Paint.Style.FILL
        styles[DrawingMode.DEFAULT.ordinal].fontPaint.color = Color.BLACK
        styles[DrawingMode.DEFAULT.ordinal].fontPaint.textSize = 30f
        /*
            editing style of lines
         */
        styles[DrawingMode.EDIT.ordinal].circuitPaint.style = Paint.Style.STROKE
        styles[DrawingMode.EDIT.ordinal].circuitPaint.color = Color.DKGRAY
        styles[DrawingMode.EDIT.ordinal].circuitPaint.strokeWidth = 10f

    }

    override fun draw(figure: Figure, drawingInformation: DrawingInformation, canvas: Canvas?) {

        figure as Edge

        Log.i("EdgeDrawingDbg", figure.toString())

        val path = Path()
        val from = figure.realBeginPoint
        val to = figure.realEndPoint
        if (from == null || to == null) return

        path.moveTo(from.x, from.y)
        path.lineTo(to.x, to.y)
        canvas?.drawPath(path, styles[drawingInformation.currentStyle].circuitPaint)
//        canvas?.drawTextOnPath("Test Text 1234567", path, 100f, 100f, styles[DrawingMode.DEFAULT.ordinal].fontPaint)
    }


}