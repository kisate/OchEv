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

    class ComparatorByPolarAngle(val mainPoint: Point) : Comparator<Point> {
        override fun compare(A: Point?, B: Point?): Int {
            if (A == null && B == null) return 0
            if (A == null) return -1
            if (B == null) return 1
            val vectorAB = Vector(mainPoint, A)
            val vectorAC = Vector(mainPoint, B)
            return vectorAB.x * vectorAC.y - vectorAB.y * vectorAC.x
        }
    }

    while (!usedPoints.containsKey(currentPoint)) {
        result.addPoint(currentPoint)
        usedPoints[currentPoint] = true

        uniquePoints.points.sortWith(ComparatorByPolarAngle(currentPoint))
        currentPoint = uniquePoints.points.first()
    }

    return result
}