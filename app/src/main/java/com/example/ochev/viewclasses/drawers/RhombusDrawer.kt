package com.example.ochev.viewclasses.drawers

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rhombus
import com.example.ochev.viewclasses.drawers.drawinginformations.DrawingInformation
import com.example.ochev.viewclasses.drawers.drawinginformations.DrawingMode

class RhombusDrawer: Drawer() {

    fun drawRhombus(rhombus: Rhombus, canvas: Canvas?, paint: Paint) {
        val path = Path()
        path.moveTo(rhombus.leftCorner.x, rhombus.leftCorner.y)
        path.lineTo(rhombus.upCorner.x, rhombus.upCorner.y)
        path.lineTo(rhombus.rightCorner.x, rhombus.rightCorner.y)
        path.lineTo(rhombus.downCorner.x, rhombus.downCorner.y)
        path.close()
        canvas?.drawPath(path, paint)
    }

    override fun draw(figure: Figure, drawingInformation: DrawingInformation, canvas: Canvas?) {
        figure as Rhombus
        drawRhombus(figure, canvas, drawingInformation.style.fillPaint)
        drawRhombus(figure, canvas, drawingInformation.style.circuitPaint)
        drawMultiLineText(figure, drawingInformation, canvas)
    }

}
