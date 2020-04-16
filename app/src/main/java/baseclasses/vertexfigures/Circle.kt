package baseclasses.vertexfigures

import baseclasses.VertexFigure
import baseclasses.dataclasses.Point

class Circle(
    text: MutableList<Char> = ArrayList(),
    texture_path: String = "",
    var center: Point = Point(),
    var radius: Int = 0
) : VertexFigure(text, texture_path)