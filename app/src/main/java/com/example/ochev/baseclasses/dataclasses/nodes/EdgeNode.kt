package com.example.ochev.baseclasses.dataclasses.nodes

import com.example.ochev.baseclasses.editors.edgefigures.Edge
import com.example.ochev.viewclasses.drawers.drawinginformations.EdgeDrawingInformation

data class EdgeNode(
    override val id: Int = 0,
    override val drawingInformation: EdgeDrawingInformation = EdgeDrawingInformation(),
    override val figure: Edge
) : FigureNode()