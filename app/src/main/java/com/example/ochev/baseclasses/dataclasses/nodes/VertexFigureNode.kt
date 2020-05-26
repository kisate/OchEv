package com.example.ochev.baseclasses.dataclasses.nodes

import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure
import com.example.ochev.viewclasses.graphdrawers.drawinginformations.DrawingInformation
import com.example.ochev.viewclasses.graphdrawers.drawinginformations.VertexDrawingInformation

data class VertexFigureNode(
    override val id: Int = 0,
    override val drawingInformation: VertexDrawingInformation,
    override val figure: VertexFigure,
    val height: Int = 0
) : FigureNode()