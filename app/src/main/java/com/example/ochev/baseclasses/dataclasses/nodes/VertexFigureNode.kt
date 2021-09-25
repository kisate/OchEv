package com.example.ochev.baseclasses.dataclasses.nodes

import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure

data class VertexFigureNode(
    override val id: Int = 0,
    override val figure: VertexFigure,
    val height: Int = 0
) : FigureNode()