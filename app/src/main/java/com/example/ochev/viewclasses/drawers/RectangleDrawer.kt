package com.example.ochev.viewclasses.drawers

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rectangle
import com.example.ochev.viewclasses.drawers.drawinginformations.DrawingInformation
import com.example.ochev.viewclasses.drawers.drawinginformations.DrawingMode

class RectangleDrawer : Drawer() {


    fun drawRect(rect: Rectangle, canvas: Canvas?, paint: Paint) {
        Log.i("ClassifyDbg", rect.toString())
        Log.i("ClassifyDbg", rect.rightDownCorner.toString() + rect.leftUpCorner.toString())
        val path = Path()
        path.moveTo(rect.rightDownCorner.x, rect.rightDownCorner.y)
        path.lineTo(rect.rightUpCorner.x, rect.rightUpCorner.y)
        path.lineTo(rect.leftUpCorner.x, rect.leftUpCorner.y)
        path.lineTo(rect.leftDownCorner.x, rect.leftDownCorner.y)
        path.close()
        canvas?.drawPath(path, paint)
    }

    override fun draw(figure: Figure, drawingInformation: DrawingInformation, canvas: Canvas?) {
        figure as Rectangle
        drawRect(figure, canvas, drawingInformation.style.fillPaint)
        drawRect(figure, canvas, drawingInformation.style.circuitPaint)
        drawMultiLineText(figure, drawingInformation, canvas)
//        canvas?.drawText(drawingInformation.text, 0, drawingInformation.text.length, textDrawingInformation.x, textDrawingInformation.y, textDrawingInformation.paint)

    }
}