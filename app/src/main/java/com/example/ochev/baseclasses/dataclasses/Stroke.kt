package com.example.ochev.baseclasses.dataclasses

data class Stroke(val points: MutableList<Point> = ArrayList()) {
    fun addPoint(pointToAdd: Point) {
        points.add(pointToAdd)
    }

    fun toFloatArray(): FloatArray {
        val result: MutableList<Float> = ArrayList()
        for (point in points) {
            result.add(point.x.toFloat())
            result.add(point.y.toFloat())
        }
        return result.toFloatArray()
    }

    fun maxX(): Int {
        return points.maxBy { it.x }!!.x
    }

    fun maxY(): Int {
        return points.maxBy { it.y }!!.y
    }

    fun minX(): Int {
        return points.minBy { it.x }!!.x
    }

    fun minY(): Int {
        return points.minBy { it.y }!!.y
    }
}

class StrokeInteractor {

    fun getStrokesRestrictions(strokes: MutableList<Stroke>): List<Int> {
        val maxX = strokes.maxBy { it.maxX() }!!.maxX()
        val minX = strokes.minBy { it.minX() }!!.minX()
        val maxY = strokes.maxBy { it.maxY() }!!.maxY()
        val minY = strokes.minBy { it.minY() }!!.minY()
        return listOf(maxX, maxY, minX, minY)
    }

}