package com.example.ochev.baseclasses.dataclasses.nodes

import com.example.ochev.baseclasses.edgefigures.Edge
import com.example.ochev.viewclasses.DrawingInformation

data class EdgeNode(
    override val id: Int = 0,
    override val drawingInformation: DrawingInformation = DrawingInformation(),
    override val figure: Edge
) : FigureNode