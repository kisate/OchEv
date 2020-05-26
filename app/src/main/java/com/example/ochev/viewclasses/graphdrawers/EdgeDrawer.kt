package com.example.ochev.viewclasses.graphdrawers

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import com.example.ochev.algorithms.ArrowGetter
import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.edgefigures.Edge
import com.example.ochev.viewclasses.graphdrawers.drawinginformations.DrawingInformation
import com.example.ochev.viewclasses.graphdrawers.drawinginformations.DrawingMode

class EdgeDrawer : Drawer() {

    override fun draw(figure: Figure, drawingInformation: DrawingInformation, canvas: Canvas?) {

        figure as Edge

        Log.i("EdgeDrawingDbg", figure.toString())

        val path = Path()
        val from = figure.realBeginPoint
        val to = figure.realEndPoint
        if (from == null || to == null) return
        path.moveTo(from.x, from.y)
        path.lineTo(to.x, to.y)
        path.addPath(ArrowGetter().getPathOfArrow(from, to))
        path.addPath(ArrowGetter().getPathOfArrow(to, from))

        if (drawingInformation.drawingMode == DrawingMode.EDIT) {
            drawingInformation.enterMode(DrawingMode.EDIT_CORNERS)
            for (point in mutableListOf<Point>(figure.realBeginPoint!!, figure.realEndPoint!!)) {
                canvas?.drawCircle(
                    point.x,
                    point.y,
                    5f,
                    drawingInformation.style.circuitPaint
                )
            }
            drawingInformation.enterMode(DrawingMode.EDIT)
        }
        canvas?.drawPath(path, drawingInformation.style.circuitPaint)
    }


}