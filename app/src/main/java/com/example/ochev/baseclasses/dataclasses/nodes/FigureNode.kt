package com.example.ochev.baseclasses.dataclasses.nodes

import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.viewclasses.graphdrawers.drawinginformations.DrawingInformation

abstract class FigureNode {
    abstract val id: Int
    abstract val drawingInformation: DrawingInformation
    abstract val figure: Figure
}