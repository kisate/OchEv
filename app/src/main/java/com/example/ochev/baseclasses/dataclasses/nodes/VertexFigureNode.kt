package com.example.ochev.baseclasses.dataclasses.nodes

import com.example.ochev.baseclasses.vertexfigures.VertexFigure
import com.example.ochev.viewclasses.DrawingInformation

data class VertexFigureNode(
    override val id: Int = 0,
    override val height: Int = 0,
    override val drawingInformation: DrawingInformation = DrawingInformation(),
    override val figure: VertexFigure
) : FigureNode