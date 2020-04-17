package baseclasses.dataclasses

import java.lang.Integer.max
import java.lang.Math.min

data class Stroke(val points: MutableList<Point> = ArrayList())

class StrokeInteractor {
    fun addPoint(stroke: Stroke, pointToAdd: Point) {
        stroke.points.add(pointToAdd)
    }

    fun maxX(stroke: Stroke): Int {
        var value = 0
        for (point in stroke.points) {
            value = max(value, point.x)
        }
        return value
    }

    fun maxY(stroke: Stroke): Int {
        var value = 0
        for (point in stroke.points) {
            value = max(value, point.y)
        }
        return value
    }

    fun minX(stroke: Stroke): Int {
        var value = Int.MAX_VALUE
        for (point in stroke.points) {
            value = min(value, point.x)
        }
        return value
    }

    fun minY(stroke: Stroke): Int {
        var value = Int.MAX_VALUE
        for (point in stroke.points) {
            value = min(value, point.y)
        }
        return value
    }

}
