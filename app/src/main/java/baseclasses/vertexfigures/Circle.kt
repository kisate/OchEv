package baseclasses.vertexfigures

import baseclasses.Point
import baseclasses.Vector
import baseclasses.VertexFigure
import java.lang.Integer.max

class Circle(
    text: MutableList<Char> = MutableList(0) { ' ' },
    texture_path: String = "",
    private var center: Point = Point(),
    private var radius: Int = 0
) : VertexFigure(text, texture_path) {
    override fun changeSize(factor: Double) {
        radius = max((radius * factor).toInt(), 1)
    }

    override fun moveByVector(direction: Vector) {
        center.x += direction.x
        center.y += direction.y
    }
}