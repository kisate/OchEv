package baseclasses

// Figure that connects information blocks

abstract class EdgeFigure(
    figureText: MutableList<Char> = ArrayList(),
    var texturePath: String = ""
) : Figure(figureText)