package baseclasses.dataclasses

data class Stroke(val points: MutableList<Point> = ArrayList())

fun Stroke.addPoint(pointToAdd: Point) {
    points.add(pointToAdd)
}