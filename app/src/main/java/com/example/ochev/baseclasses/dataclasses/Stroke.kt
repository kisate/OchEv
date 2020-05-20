package com.example.ochev.baseclasses.dataclasses

data class Stroke(val points: MutableList<Point> = ArrayList()) {
    fun addPoint(pointToAdd: Point) {
        points.add(pointToAdd)
    }

    fun copy(): Stroke {
        val result = Stroke()
        points.forEach { result.addPoint(it.copy()) }
        return result
    }

    fun toFloatArray(): FloatArray {
        val result: MutableList<Float> = ArrayList()
        for (point in points) {
            result.add(point.x.toFloat())
            result.add(point.y.toFloat())
        }
        return result.toFloatArray()
    }

    fun maxX(): Float {
        return points.maxBy { it.x }!!.x
    }

    fun maxY(): Float {
        return points.maxBy { it.y }!!.y
    }

    fun minX(): Float {
        return points.minBy { it.x }!!.x
    }

    fun minY(): Float {
        return points.minBy { it.y }!!.y
    }

    companion object {
        fun getStrokesRestrictions(strokes: MutableList<Stroke>): MutableList<Float> {
            val maxX = strokes.maxBy { it.maxX() }!!.maxX()
            val minX = strokes.minBy { it.minX() }!!.minX()
            val maxY = strokes.maxBy { it.maxY() }!!.maxY()
            val minY = strokes.minBy { it.minY() }!!.minY()
            return mutableListOf(maxX, maxY, minX, minY)
        }

        fun joinListOfStrokes(strokes: MutableList<Stroke>): Stroke {
            val points: HashMap<Point, Boolean> = HashMap()
            val result = Stroke()
            strokes.forEach { stroke ->
                stroke.points.forEach { point ->
                    if (!points.containsKey(point)) result.addPoint(point)
                    points[point] = true
                }
            }
            return result
        }
    }
}

