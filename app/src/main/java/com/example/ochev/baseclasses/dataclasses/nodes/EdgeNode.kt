package com.example.ochev.baseclasses.dataclasses.nodes

import com.example.ochev.baseclasses.dataclasses.edgefigures.Edge
import com.example.ochev.viewclasses.graphdrawers.drawinginformations.DrawingInformation
import com.example.ochev.viewclasses.graphdrawers.drawinginformations.EdgeDrawingInformation

data class EdgeNode(
    override val id: Int = 0,
    override val drawingInformation: EdgeDrawingInformation = EdgeDrawingInformation(),
    override val figure: Edge
) : FigureNode()