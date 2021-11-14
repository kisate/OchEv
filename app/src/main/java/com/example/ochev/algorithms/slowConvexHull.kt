package com.example.ochev.algorithms

import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.Stroke.Companion.joinListOfStrokes
import com.example.ochev.baseclasses.dataclasses.Vector
import kotlin.math.abs


fun ConvexHullMaker.slowConvexHull(strokes: MutableList<Stroke>): Stroke {
    val uniquePoints = joinListOfStrokes(strokes)
    val result = Stroke()

    val usedPoints: HashMap<Point, Boolean> = HashMap()
    var currentPoint = uniquePoints.points.maxByOrNull { it.y }!!

    class ComparatorByPolarAngle(val mainPoint: Point) : Comparator<Point> {
        override fun compare(A: Point?, B: Point?): Int {
            if (A == null && B == null) return 0
            if (A == null || A == mainPoint) return -1
            if (B == null || B == mainPoint) return 1
            val vectorAB = Vector(mainPoint, A)
            val vectorAC = Vector(mainPoint, B)
            val product = vectorAB.x * vectorAC.y - vectorAB.y * vectorAC.x
            if (abs(product) <= 0.0001) return 0
            return if (product > 0) 1 else -1
        }
    }

    while (!usedPoints.containsKey(currentPoint)) {
        result.addPoint(currentPoint)
        usedPoints[currentPoint] = true

        uniquePoints.points.sortWith(ComparatorByPolarAngle(currentPoint))
        currentPoint = uniquePoints.points.last()
    }

    return result
}