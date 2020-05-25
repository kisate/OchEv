package com.example.ochev.viewclasses.graphdrawers

import android.graphics.Canvas
import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.edgefigures.Edge
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Circle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rectangle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rhombus
import com.example.ochev.viewclasses.DrawingInformation

class FiguresDrawer {
    val circleDrawer = CircleDrawer()
    val rectangleDrawer = RectangleDrawer()
    val edgeDrawer = EdgeDrawer()
    val rhombusDrawer = RhombusDrawer()

    fun draw(figure: Figure, drawingInformation: DrawingInformation, canvas: Canvas?) {
        when (figure) {
            is Circle -> {
                circleDrawer.draw(figure, drawingInformation,canvas)
            }
            is Rectangle -> {
                rectangleDrawer.draw(figure, drawingInformation, canvas)
            }
            is Edge -> {
                edgeDrawer.draw(figure, drawingInformation, canvas)
            }
            is Rhombus -> {
                rhombusDrawer.draw(figure, drawingInformation, canvas)
            }
        }
    }
}