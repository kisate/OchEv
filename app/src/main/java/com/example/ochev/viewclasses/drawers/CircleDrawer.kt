package com.example.ochev.viewclasses.drawers

import android.graphics.Canvas
import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Circle
import com.example.ochev.viewclasses.drawers.drawinginformations.CircleDrawingInformation
import com.example.ochev.viewclasses.drawers.drawinginformations.DrawingInformation
import com.example.ochev.viewclasses.drawers.drawinginformations.DrawingMode

class CircleDrawer : Drawer() {

    override fun draw(figure: Figure, drawingInformation: DrawingInformation, canvas: Canvas?) {

        figure as Circle
        drawingInformation as CircleDrawingInformation

        canvas?.drawCircle(
            figure.center.x,
            figure.center.y,
            figure.radius,
            drawingInformation.style.fillPaint
        )
        canvas?.drawCircle(
            figure.center.x,
            figure.center.y,
            figure.radius,
            drawingInformation.style.circuitPaint
        )
        drawMultiLineText(figure, drawingInformation, canvas)

    }


}