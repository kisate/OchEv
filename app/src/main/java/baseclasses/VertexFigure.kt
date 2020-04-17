package baseclasses

/*
A figure, that represents an information block in our scheme
 */

abstract class VertexFigure(
    figureText: MutableList<Char> = ArrayList(),
    var texturePath: String = ""
) : Figure(figureText)
