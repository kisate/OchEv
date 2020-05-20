package com.example.ochev.baseclasses.dataclasses

import com.example.ochev.baseclasses.Figure
import com.example.ochev.viewclasses.DrawingInformation

data class DrawingFigure(
    val figure: Figure,
    val drawingInformation: DrawingInformation = DrawingInformation()
) {
}