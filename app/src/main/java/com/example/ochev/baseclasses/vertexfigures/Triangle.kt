package com.example.ochev.baseclasses.vertexfigures

import com.example.ochev.algorithms.convexhull.ConvexHullMaker
import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.Vector

class Triangle(
    var pointA: Point = Point(),
    var pointB: Point = Point(),
    var pointC: Point = Point()
) : VertexFigure() {
    override val center: Point
        get() = Point(
            x = (pointA.x + pointB.x + pointC.x) / 3,
            y = (pointA.y + pointB.y + pointC.y) / 3
        )

    override fun moveByVector(vector: Vector) {
        pointA.moveByVector(vector)
        pointB.moveByVector(vector)
        pointC.moveByVector(vector)
    }

    override fun getDistanceToPoint(point: Point): Float {
        // if the triangle has points ABC, we check dist till line segment AB,BC,CA and take min

        // A -> B
        val tillAB = point.getDistanceToLineSegment(pointA, pointB)
        // B -> C
        val tillBC = point.getDistanceToLineSegment(pointB, pointC)
        // C -> A
        val tillAC = point.getDistanceToLineSegment(pointC, pointA)

        return listOf(tillAB, tillAC, tillBC).min()!!
    }

    override fun checkIfPointIsInside(point: Point): Boolean {

        // we have triangle ABC, A = (0,0), P is checking point in new coordinates
        var vectorB = Vector(pointA, pointB)
        var vectorC = Vector(pointA, pointC)
        val vectorP = Vector(pointA, point)

        if (vectorB.y == 0) vectorB = vectorC.also { vectorC = vectorB }

        val cordAlongVectorC =
            (vectorP.x - vectorB.x * vectorP.y / vectorB.y.toFloat()) /
                    (vectorC.x - vectorC.y * vectorB.x / vectorB.y.toFloat())

        val cordAlongVectorB =
            (vectorP.y - cordAlongVectorC * vectorC.y) / vectorC.y.toFloat()

        return cordAlongVectorB >= 0 &&
                cordAlongVectorC >= 0 &&
                cordAlongVectorB + cordAlongVectorC <= 1
    }
}

fun VertexFigureNormalizer.normalizeTriangle(strokes: MutableList<Stroke>): Triangle {
    val convexHullMaker = ConvexHullMaker()
    val convexHull = convexHullMaker.getConvexHull(strokes)

    val getSquare = { A: Point, B: Point, C: Point ->
        val vectorAB = Vector(A, B)
        val vectorAC = Vector(A, C)
        (vectorAB.x * vectorAC.y - vectorAB.y * vectorAC.x) / 2f
    }


    val result = Triangle()
    var currentSquare = 0f
    val totalyPoints = convexHull.points.size


    // n^2 algorithm with two pointers to find a triangle with the biggest square

    for (firstPointIndex in convexHull.points.indices) {
        var bestChoiceIndex = (firstPointIndex + 2) % totalyPoints

        val firstPoint = convexHull.points[firstPointIndex]

        for (shiftFromFirstPoint in 1 until totalyPoints) {
            val secondPoint =
                convexHull.points[(firstPointIndex + shiftFromFirstPoint) % totalyPoints]
            var bestPoint: Point

            while (true) {
                val possibleBestPoint = convexHull.points[(bestChoiceIndex + 1) % totalyPoints]
                bestPoint = convexHull.points[bestChoiceIndex]

                if (getSquare(
                        possibleBestPoint,
                        firstPoint,
                        secondPoint
                    ) -
                    getSquare(
                        bestPoint,
                        firstPoint,
                        secondPoint
                    ) >= 0.0001f
                ) {
                    bestChoiceIndex = (bestChoiceIndex + 1) % totalyPoints
                } else break
            }

            val newSquare = getSquare(
                bestPoint,
                firstPoint,
                secondPoint
            )

            if (newSquare > currentSquare) {
                currentSquare = newSquare
                result.pointA = bestPoint
                result.pointB = firstPoint
                result.pointC = secondPoint
            }
        }
    }

    return result
}