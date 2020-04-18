package baseclasses.edgefigures

import baseclasses.EdgeFigure
import baseclasses.VertexFigure

class Line(
    figureText: MutableList<Char> = ArrayList(),
    val beginFigure: VertexFigure,
    val endFigure: VertexFigure
) : EdgeFigure(figureText)
