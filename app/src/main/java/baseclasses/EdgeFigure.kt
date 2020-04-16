package baseclasses

// Figure that connects information blocks

abstract class EdgeFigure(
    text: MutableList<Char> = ArrayList(),
    var colour: Int = 0
) : Figure(text)