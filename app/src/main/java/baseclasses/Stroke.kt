package baseclasses

import baseclasses.edgefigures.Line

class Stroke(
    private val _points: MutableList<Point> = MutableList<Point>(0, { it -> Point() })
) {
    val points: List<Point>
        get() = _points.toList()

    fun addPoint(point_to_add: Point) {
        _points.add(point_to_add)
    }

    fun vectorize(): Line {
        TODO()
    }
}