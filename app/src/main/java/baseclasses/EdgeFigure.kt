package baseclasses

// Figure that connects information blocks

abstract class EdgeFigure(
    text: MutableList<Char> = ArrayList(),
    var texturePath: String = ""
) : Figure(text)