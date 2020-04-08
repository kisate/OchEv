package baseclasses.vertexfigures

import baseclasses.ConnectingSpot
import baseclasses.Point
import baseclasses.Vector
import baseclasses.VertexFigure
import java.lang.Integer.max

class Circle(
    text: MutableList<Char> = ArrayList(),
    texture_path: String = "",
    private var center: Point = Point(),
    private var radius: Int = 0
) : VertexFigure(text, texture_path) {
    // circle has 8 connecting spots
    override val spots: List<ConnectingSpot> = List(8) { ConnectingSpot(it, this) }

    override fun changeSize(factor: Double) {
        radius = max((radius * factor).toInt(), 1)
    }

    override fun moveByVector(direction: Vector) {
        center.x += direction.x
        center.y += direction.y
    }

    override fun getSpotY(id: Int): Int {
        TODO("Not yet implemented")
    }

    override fun getSpotX(id: Int): Int {
        TODO("Not yet implemented")
    }
}