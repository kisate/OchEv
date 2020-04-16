package baseclasses.vertexfigures

import baseclasses.VertexFigure
import baseclasses.dataclasses.Point

class Rectangle(
    text: MutableList<Char> = ArrayList(),
    texture_path: String = "",
    var left_corner: Point = Point(),
    var height: Int = 0,
    var width: Int = 0
) : VertexFigure(text, texture_path)