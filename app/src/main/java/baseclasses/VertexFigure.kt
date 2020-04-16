package baseclasses

/*
A figure, that represents an information block in our scheme
 */

open class VertexFigure(
    text: MutableList<Char> = ArrayList(),
    var _texture_path: String = ""
) : Figure(text)