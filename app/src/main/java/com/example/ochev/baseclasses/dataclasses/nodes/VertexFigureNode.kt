package com.example.ochev.baseclasses.dataclasses.nodes

import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure

data class TextInfo(
    val leftDownCorner: Point = Point(),
    val rightUpCorner: Point = Point(),
    val changed: Boolean = false,
    val text: String = "",
    val fontSize: Int = 10,
) {
    fun update(newFigure: VertexFigure): TextInfo {
        return copy(
            leftDownCorner = newFigure.getLeftDownDrawingCorner(),
            rightUpCorner = newFigure.getRightUpDrawingCorner(),
            changed = true
        )
    }
}

data class VertexFigureNode(
    override val id: Int = 0,
    override val figure: VertexFigure,
    val height: Int = 0,
    val textInfo: TextInfo = TextInfo(
        leftDownCorner = figure.drawingPoints[0],
        rightUpCorner = figure.drawingPoints[1]
    )
) : FigureNode()