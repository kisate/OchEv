package baseclasses.dataclasses

data class Stroke(val points: MutableList<Point> = ArrayList())

class StrokeInteractor {
    fun addPoint(stroke: Stroke, pointToAdd: Point) {
        stroke.points.add(pointToAdd)
    }

    fun maxX(stroke: Stroke): Int {
        return stroke.points.maxBy { it.x }!!.x
    }

    fun maxY(stroke: Stroke): Int {
        return stroke.points.maxBy { it.y }!!.y
    }

    fun minX(stroke: Stroke): Int {
        return stroke.points.minBy { it.x }!!.x
    }

    fun minY(stroke: Stroke): Int {
        return stroke.points.minBy { it.y }!!.y
    }

    fun getStrokesRestrictions(strokes: MutableList<Stroke>): List<Int> {
        val maxX = strokes.maxBy { maxX(it) }!!.let { maxX(it) }
        val minX = strokes.minBy { minX(it) }!!.let { minX(it) }
        val maxY = strokes.maxBy { maxY(it) }!!.let { maxY(it) }
        val minY = strokes.minBy { minY(it) }!!.let { minY(it) }
        return listOf(maxX, maxY, minX, minY)
    }

    fun toFloatArray(stroke: Stroke): FloatArray {
        val result: MutableList<Float> = ArrayList()
        for (point in stroke.points) {
            result.add(point.x.toFloat())
            result.add(point.y.toFloat())
        }
        return result.toFloatArray()
    }

}
