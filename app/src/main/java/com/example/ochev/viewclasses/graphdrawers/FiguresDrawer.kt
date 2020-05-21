package com.example.ochev.viewclasses.graphdrawers

import android.graphics.Canvas
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.edgefigures.Edge
import com.example.ochev.baseclasses.vertexfigures.Circle
import com.example.ochev.baseclasses.vertexfigures.Rectangle
import com.example.ochev.baseclasses.vertexfigures.VertexFigure
import com.example.ochev.viewclasses.DrawingInformation

class FiguresDrawer {
    val circleDrawer = CircleDrawer()
    val rectangleDrawer = RectangleDrawer()
    val edgeDrawer = EdgeDrawer()

    fun draw(figure: Figure, drawingInformation: DrawingInformation, canvas: Canvas?) {
        when (figure) {
            is Circle -> {
                circleDrawer.draw(figure, drawingInformation,canvas)
                circleDrawer.drawText(figure, drawingInformation, canvas, circleDrawer.styles[drawingInformation.currentStyle].fontPaint)
            }
            is Rectangle -> {
                rectangleDrawer.draw(figure, drawingInformation, canvas)
                rectangleDrawer.drawText(figure,drawingInformation,canvas,rectangleDrawer.styles[drawingInformation.currentStyle].fontPaint)
            }
            is Edge -> {
                edgeDrawer.draw(figure, drawingInformation, canvas)
            }
        }
    }
}