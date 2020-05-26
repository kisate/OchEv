package com.example.ochev.viewclasses.drawers

import android.graphics.Canvas
import android.graphics.Path
import android.util.Log
import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.edgefigures.Edge
import com.example.ochev.viewclasses.drawers.drawinginformations.DrawingInformation
import com.example.ochev.viewclasses.drawers.drawinginformations.DrawingMode
import com.example.ochev.viewclasses.drawers.drawinginformations.EdgeDrawingInformation

class EdgeDrawer : Drawer() {

    private val arrowheadDrawer = ArrowheadDrawer()
    private val editingArrowheadDrawer = EditingArrowheadDrawer()


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
        canvas?.drawPath(path, drawingInformation.style.circuitPaint)

        if (drawingInformation.types[1] == 1) {
            arrowheadDrawer.draw(from, to, canvas)
        }
        if (drawingInformation.types[0] == 1){
            arrowheadDrawer.draw(to, from, canvas)
        }
        if (drawingInformation.drawingMode == DrawingMode.EDIT) {
            editingArrowheadDrawer.draw(from, to, canvas)
            editingArrowheadDrawer.draw(to, from, canvas)
        }
    }


}