package baseclasses.vertexfigures

import baseclasses.FigureInteractor
import baseclasses.VertexFigure
import baseclasses.VertexFigureNormalizer
import baseclasses.dataclasses.*

class Triangle(
    figureText: MutableList<Char> = ArrayList(),
    texturePath: String = "",
    var pointA: Point = Point(),
    var pointB: Point = Point(),
    var pointC: Point = Point()
) : VertexFigure(figureText, texturePath)


fun FigureInteractor.changeSize(triangle: Triangle) {
    TODO()
}

fun FigureInteractor.moveByVector(triangle: Triangle, direction: Vector) {
    triangle.pointA.x += direction.x
    triangle.pointA.y += direction.y
    triangle.pointB.x += direction.x
    triangle.pointB.y += direction.y
    triangle.pointC.x += direction.x
    triangle.pointC.y += direction.y
}

fun FigureInteractor.getCenter(triangle: Triangle): Point {
    return Point(
        x = (triangle.pointA.x + triangle.pointB.x + triangle.pointC.x) / 3,
        y = (triangle.pointA.y + triangle.pointB.y + triangle.pointC.y) / 3
    )
}

fun FigureInteractor.getDistanceBetweenFigureAndPoint(triangle: Triangle, point: Point): Float {
    val pointInteractor = PointInteractor()

    // we have triangle ABC
    // check distance till line segments AB,BC,AC, take min

    // AB

    val tillFirstLine = pointInteractor.getDistanceBetweenPointAndLineSegment(
        point,
        triangle.pointA,
        triangle.pointB
    )

    // BC

    val tillSecondLine = pointInteractor.getDistanceBetweenPointAndLineSegment(
        point,
        triangle.pointB,
        triangle.pointC
    )

    // AC

    val tillThirdLine = pointInteractor.getDistanceBetweenPointAndLineSegment(
        point,
        triangle.pointC,
        triangle.pointA
    )

    return listOf(
        tillFirstLine,
        tillSecondLine,
        tillThirdLine
    ).min()!!

}

fun VertexFigureNormalizer.normalizeTriangle(strokes: MutableList<Stroke>): Triangle {
    val strokeInteractor = StrokeInteractor()
    val pointInteractor = PointInteractor()

    // first point , it is on the top of the triangle
    val pointWithMaxY =
        strokes.maxBy { strokeInteractor.maxY(it) }!!.let { it.points.maxBy { it.y }!! }

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