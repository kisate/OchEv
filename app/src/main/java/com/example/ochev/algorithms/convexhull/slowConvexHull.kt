package com.example.ochev.algorithms.convexhull

import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.StrokeInteractor
import com.example.ochev.baseclasses.dataclasses.Vector

fun ConvexHullMaker.slowConvexHull(strokes: MutableList<Stroke>): Stroke {
    val strokeInteractor = StrokeInteractor()
    val uniquePoints = strokeInteractor.joinListOfStrokes(strokes)
    val result = Stroke()

    val usedPoints: HashMap<Point, Boolean> = HashMap()
    var currentPoint = uniquePoints.points.maxBy { it.y }!!

    while (!usedPoints.containsKey(currentPoint)) {
        result.addPoint(currentPoint)
        usedPoints[currentPoint] = true

        currentPoint = uniquePoints.points.minBy { point ->
            val vector = Vector(currentPoint, point)
            vector.x / (vector.y + 0.00001)
        }!!
    }

    return result
}