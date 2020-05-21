package com.example.ochev.viewclasses.graphdrawers

import android.graphics.Canvas
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.edgefigures.Edge
import com.example.ochev.baseclasses.vertexfigures.Circle
import com.example.ochev.baseclasses.vertexfigures.Rectangle
import com.example.ochev.viewclasses.DrawingInformation

class FiguresDrawer {
    val circleDrawer = CircleDrawer()
    val rectangleDrawer =
        RectangleDrawer()
    val edgeDrawer = EdgeDrawer()

    fun draw(figure: Figure, drawingInformation: DrawingInformation, canvas: Canvas?) {
        when (figure) {
            is Circle -> {
                circleDrawer.currentStyle = drawingInformation.drawingMode.ordinal
                circleDrawer.draw(figure, drawingInformation,canvas)
            }
            is Rectangle -> {
                rectangleDrawer.currentStyle = drawingInformation.drawingMode.ordinal
                rectangleDrawer.draw(figure, drawingInformation, canvas)
            }
            is Edge -> {
                edgeDrawer.currentStyle = drawingInformation.drawingMode.ordinal
                edgeDrawer.draw(figure, drawingInformation, canvas)
            }
        }
    }
}