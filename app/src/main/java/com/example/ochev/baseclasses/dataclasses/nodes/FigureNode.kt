package com.example.ochev.baseclasses.dataclasses.nodes

import com.example.ochev.baseclasses.Figure
import com.example.ochev.viewclasses.DrawingInformation

interface FigureNode {
    val id: Int
    val drawingInformation: DrawingInformation
    val figure: Figure
}