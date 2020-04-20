package com.example.ochev.baseclasses.vertexfigures

import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.VertexFigureNormalizer
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.PointInteractor
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.Vector

class Triangle(
    figureText: MutableList<Char> = ArrayList(),
    texturePath: String = "",
    var pointA: Point = Point(),
    var pointB: Point = Point(),
    var pointC: Point = Point()
) : VertexFigure(figureText, texturePath) {
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
}

fun VertexFigureNormalizer.normalizeTriangle(strokes: MutableList<Stroke>): Triangle {
    val pointInteractor = PointInteractor()

    // first point , it is on the top of the triangle
    val pointWithMaxY =
        strokes.maxBy { it.maxY() }!!.let { it.points.maxBy { it.y }!! }

    // second point is the most distanced from the first
    val mostDistancedPoint =
        strokes.maxBy { stroke ->
            stroke.points.maxBy { point ->
                pointInteractor.getDistanceBetweenTwoPoints(point, pointWithMaxY)
            }!!.let { point ->
                pointInteractor.getDistanceBetweenTwoPoints(point, pointWithMaxY)
            }
        }!!.let { stroke ->
            stroke.points.maxBy { point ->
                pointInteractor.getDistanceBetweenTwoPoints(point, pointWithMaxY)
            }!!
        }

    // now we find third point of triangle
    // sum of distances from first and second mast be max

    val thirdPoint = strokes.maxBy { stroke ->
        stroke.points.maxBy { point ->
            pointInteractor.getDistanceBetweenTwoPoints(point, pointWithMaxY) +
                    pointInteractor.getDistanceBetweenTwoPoints(point, mostDistancedPoint)
        }!!.let { point ->
            pointInteractor.getDistanceBetweenTwoPoints(point, pointWithMaxY) +
                    pointInteractor.getDistanceBetweenTwoPoints(point, mostDistancedPoint)
        }
    }!!.let { stroke ->
        stroke.points.maxBy { point ->
            pointInteractor.getDistanceBetweenTwoPoints(point, pointWithMaxY) +
                    pointInteractor.getDistanceBetweenTwoPoints(point, mostDistancedPoint)
        }!!
    }

    return Triangle(
        pointA = pointWithMaxY,
        pointB = mostDistancedPoint,
        pointC = thirdPoint
    )

}