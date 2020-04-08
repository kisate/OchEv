package baseclasses.vertexfigures

import baseclasses.Point
import baseclasses.Vector
import baseclasses.VertexFigure
import java.lang.Integer.max

class Rectangle(
    text: MutableList<Char> = MutableList(0) { ' ' },
    texture_path: String = "",
    private var left_corner: Point = Point(),
    private var height: Int = 0,
    private var width: Int = 0
) : VertexFigure(text, texture_path) {
    override fun changeSize(factor: Double) {
        height = max((height * factor).toInt(), 1)
        width = max((width * factor).toInt(), 1)
    }

    override fun moveByVector(direction: Vector) {
        left_corner.x += direction.x
        left_corner.y += direction.y
    }
}