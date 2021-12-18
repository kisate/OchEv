package com.example.ochev.baseclasses.dataclasses.nodes

import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure

data class TextInfo(
    val leftDownCorner: Point = Point(0f, 300f),
    val rightUpCorner: Point = Point(300f, 0f),
    var changed: Boolean = false,
    var text: String = ""
)

data class VertexFigureNode(
    override val id: Int = 0,
    override val figure: VertexFigure,
    val height: Int = 0,
    val textInfo: TextInfo = TextInfo()
) : FigureNode()