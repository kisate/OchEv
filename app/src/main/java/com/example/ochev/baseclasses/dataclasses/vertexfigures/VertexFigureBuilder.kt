package com.example.ochev.baseclasses.dataclasses.vertexfigures

import com.example.ochev.baseclasses.dataclasses.Stroke

class VertexFigureBuilder {

    fun buildFigure(strokes: MutableList<Stroke>, type: Vertexes): VertexFigure {
        return when (type) {
            Vertexes.RECTANGLE -> buildRectangle(strokes)
            Vertexes.CIRCLE -> buildCircle(strokes)
            Vertexes.RHOMBUS -> buildRhombus(strokes)
        }
    }
}

