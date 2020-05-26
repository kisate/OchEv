package com.example.ochev.viewclasses.graphdrawers.drawinginformations

import com.example.ochev.baseclasses.dataclasses.vertexfigures.Circle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rectangle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rhombus
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure
import com.example.ochev.viewclasses.graphdrawers.FigureStyle

enum class DrawingMode {
    DEFAULT,
    EDIT,
    EDIT_CORNERS;
}

interface DrawingInformation {
    var drawingMode: DrawingMode
    var text: String
    var style: FigureStyle
    fun enterMode(newDrawingMode: DrawingMode) {
        drawingMode = newDrawingMode
    }

    companion object {
        fun getVertexDrawingInformation(figure: VertexFigure): VertexDrawingInformation? {
            return when (figure) {
                is Circle -> {
                    CircleDrawingInformation()
                }
                is Rectangle -> {
                    RectangleDrawingInformation()
                }
                is Rhombus -> {
                    RhombusDrawingInformation()
                }
                else -> {
                    null
                }
            }
        }

        fun getEdgeDrawingInformation(): EdgeDrawingInformation? {
            return EdgeDrawingInformation()
        }
    }
}