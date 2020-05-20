package com.example.ochev.baseclasses.editors.vertexeditor

import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.vertexfigures.VertexFigure

data class PointMover(
    val point: Point,
    val moveFun: (point: Point) -> VertexFigure
)