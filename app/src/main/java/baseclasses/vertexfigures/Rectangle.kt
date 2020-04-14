package baseclasses.vertexfigures

import baseclasses.ConnectingSpot
import baseclasses.Point
import baseclasses.Vector
import baseclasses.VertexFigure
import java.lang.Integer.max

class Rectangle(
    text: MutableList<Char> = ArrayList(),
    texture_path: String = "",
    private var left_corner: Point = Point(),
    private var height: Int = 0,
    private var width: Int = 0
) : VertexFigure(text, texture_path) {
    // rectangle has 8 connecting spots
    override val spots: List<ConnectingSpot> = List(8) { ConnectingSpot(it, this) }

    override fun changeSize(factor: Double) {
        height = max((height * factor).toInt(), 1)
        width = max((width * factor).toInt(), 1)
    }

    override fun moveByVector(direction: Vector) {
        left_corner.x += direction.x
        left_corner.y += direction.y
    }

    override fun getSpotY(id: Int): Int {
        TODO("Not yet implemented")
    }

    override fun getSpotX(id: Int): Int {
        TODO("Not yet implemented")
    }
}