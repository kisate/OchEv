package com.example.ochev.viewclasses.graphdrawers

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Circle
import com.example.ochev.viewclasses.graphdrawers.drawinginformations.CircleDrawingInformation
import com.example.ochev.viewclasses.graphdrawers.drawinginformations.DrawingInformation
import com.example.ochev.viewclasses.graphdrawers.drawinginformations.DrawingMode

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
        if (drawingInformation.drawingMode == DrawingMode.EDIT) {
            drawingInformation.enterMode(DrawingMode.EDIT_CORNERS)
            for (point in figure.getMovingPoints()) {
                canvas?.drawCircle(
                    point.x,
                    point.y,
                    5f,
                    drawingInformation.style.circuitPaint
                )
            }
            drawingInformation.enterMode(DrawingMode.EDIT)
        }

    }


}