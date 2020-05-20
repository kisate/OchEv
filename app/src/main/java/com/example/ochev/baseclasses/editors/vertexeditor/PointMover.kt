package com.example.ochev.baseclasses.editors.vertexeditor

import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.Point

data class PointMover(
    val point: Point,
    val moveFun: (point: Point) -> VertexFigure
)