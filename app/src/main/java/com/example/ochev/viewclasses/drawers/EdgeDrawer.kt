package com.example.ochev.viewclasses.drawers

import android.graphics.Canvas
import android.graphics.Path
import android.util.Log
import com.example.ochev.algorithms.ArrowGetter
import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.edgefigures.Edge
import com.example.ochev.viewclasses.drawers.drawinginformations.DrawingInformation
import com.example.ochev.viewclasses.drawers.drawinginformations.DrawingMode
import com.example.ochev.viewclasses.drawers.drawinginformations.EdgeDrawingInformation

class EdgeDrawer : Drawer() {

    override fun draw(figure: Figure, drawingInformation: DrawingInformation, canvas: Canvas?) {

        figure as Edge
        drawingInformation as EdgeDrawingInformation

        Log.i("EdgeDrawingDbg", figure.toString())

        val path = Path()
        val from = figure.realBeginPoint
        val to = figure.realEndPoint
        if (from == null || to == null) return
        path.moveTo(from.x, from.y)
        path.lineTo(to.x, to.y)
        if (drawingInformation.types[0] == 1)
            path.addPath(ArrowGetter().getPathOfArrow(from, to))
        if (drawingInformation.types[1] == 1)
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