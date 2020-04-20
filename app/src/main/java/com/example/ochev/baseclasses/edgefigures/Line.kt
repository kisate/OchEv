package com.example.ochev.baseclasses.edgefigures

import com.example.ochev.baseclasses.EdgeFigure
import com.example.ochev.baseclasses.VertexFigure

class Line(
    figureText: MutableList<Char> = ArrayList(),
    val beginFigure: VertexFigure,
    val endFigure: VertexFigure
) : EdgeFigure(figureText)
