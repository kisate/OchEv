package baseclasses.edgefigures

import baseclasses.EdgeFigure

class Line(
    figureText: MutableList<Char> = ArrayList(),
    texturePath: String = ""
) : EdgeFigure(figureText, texturePath)
