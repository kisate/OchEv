package baseclasses.dataclasses

data class Stroke(val points: MutableList<Point> = ArrayList())

class StrokeInteractor {
    fun addPoint(stroke: Stroke, pointToAdd: Point) {
        stroke.points.add(pointToAdd)
    }
}
