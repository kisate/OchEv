package com.example.ochev.viewclasses.drawers

import android.graphics.Canvas
import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.edgefigures.Edge
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Circle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rectangle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rhombus
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure
import com.example.ochev.viewclasses.drawers.drawinginformations.DrawingInformation

class FiguresDrawer {
    private val circleDrawer = CircleDrawer()
    private val rectangleDrawer = RectangleDrawer()
    private val edgeDrawer = EdgeDrawer()
    private val rhombusDrawer = RhombusDrawer()

    fun draw(figure: Figure, drawingInformation: DrawingInformation, canvas: Canvas?) {
        when (figure) {
            is VertexFigure -> {
                when (figure) {
                    is Circle -> {
                        circleDrawer.draw(figure, drawingInformation, canvas)
                        circleDrawer.drawEditingPoints(drawingInformation, figure.getMovingPoints(), canvas)
                    }
                    is Rectangle -> {
                        rectangleDrawer.draw(figure, drawingInformation, canvas)
                        rectangleDrawer.drawEditingPoints(drawingInformation, figure.getMovingPoints(), canvas)
                    }
                    is Rhombus -> {
                        rhombusDrawer.draw(figure, drawingInformation, canvas)
                        rhombusDrawer.drawEditingPoints(drawingInformation, figure.getMovingPoints(), canvas)
                    }
                }
            }
            is Edge -> {
                edgeDrawer.draw(figure, drawingInformation, canvas)
                if (figure.realBeginPoint == null || figure.realEndPoint == null)return
                edgeDrawer.drawEditingPoints(drawingInformation, mutableListOf(figure.realBeginPoint!!, figure.realEndPoint!!), canvas)
            }
        }
    }
}