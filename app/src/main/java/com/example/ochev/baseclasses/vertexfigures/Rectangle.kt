package com.example.ochev.baseclasses.vertexfigures

import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.Stroke.Companion.getStrokesRestrictions
import com.example.ochev.baseclasses.dataclasses.Vector
import com.example.ochev.baseclasses.editors.vertexeditor.PointMover
import kotlin.math.max
import kotlin.math.min


data class Rectangle(
    val leftDownCorner: Point = Point(),
    val rightUpCorner: Point = Point()
) : VertexFigure() {
    val leftUpCorner: Point
        get() = Point(leftDownCorner.x, rightUpCorner.y)

    val rightDownCorner: Point
        get() = Point(leftDownCorner.y, rightUpCorner.x)


    override val center
        get() =
            Point(
                x = (leftDownCorner.x + rightUpCorner.x) / 2,
                y = (leftDownCorner.y + rightUpCorner.y) / 2
            )

    override fun movedByVector(vector: Vector): VertexFigure {
        return this.copy(
            leftDownCorner = leftDownCorner.movedByVector(vector),
            rightUpCorner = rightUpCorner.movedByVector(vector)
        )
    }

    override fun getDistanceToPoint(point: Point): Float {
        // if the rectangle has points ABCD, we check dist till line segment AB,BC,CD,AD and take min
        // A - leftDown, B - leftUp, C - rightUp, D - rightDown

        // leftDown -> leftUp
        val tillAB = point.getDistanceToLineSegment(leftDownCorner, leftUpCorner)
        // leftUp -> rightUp
        val tillBC = point.getDistanceToLineSegment(leftUpCorner, rightUpCorner)
        // rightUp -> rightDown
        val tillCD = point.getDistanceToLineSegment(rightUpCorner, rightDownCorner)
        // rightDown -> leftDown
        val tillAD = point.getDistanceToLineSegment(rightDownCorner, leftDownCorner)

        return listOf(tillAB, tillBC, tillCD, tillAD).min()!!
    }

    override fun checkIfPointIsInside(point: Point): Boolean {
        val maxX = max(leftDownCorner.x, rightDownCorner.x)
        val minX = min(leftDownCorner.x, rightDownCorner.x)
        val maxY = max(leftDownCorner.y, leftUpCorner.y)
        val minY = min(leftDownCorner.y, leftUpCorner.y)

        return (point.x in minX..maxX && point.y in minY..maxY)
    }

    override fun getPointMovers(): MutableList<PointMover> {
        val result: MutableList<PointMover> = ArrayList()
        val points = getMovingPoints()

        // add angle movers
        result.add(PointMover(
            points[0]
        )
        { point: Point ->
            this.copy(
                leftDownCorner = point
            )
        })

        result.add(PointMover(
            points[1]
        )
        { point: Point ->
            this.copy(
                leftDownCorner = Point(point.x, leftDownCorner.y),
                rightUpCorner = Point(rightUpCorner.x, point.y)
            )
        })

        result.add(PointMover(
            points[2]
        )
        { point: Point ->
            this.copy(
                rightUpCorner = point
            )
        })

        result.add(PointMover(
            points[3]
        )
        { point: Point ->
            this.copy(
                leftDownCorner = Point(leftDownCorner.x, point.y),
                rightUpCorner = Point(point.x, rightUpCorner.y)
            )
        })

        // add side points

        result.add(PointMover(
            points[4]
        )
        { point: Point ->
            this.copy(
                leftDownCorner = Point(point.x, leftDownCorner.y)
            )
        })

        result.add(PointMover(
            points[5]
        )
        { point: Point ->
            this.copy(
                rightUpCorner = Point(rightUpCorner.x, point.y)
            )
        })

        result.add(PointMover(
            points[6]
        )
        { point: Point ->
            this.copy(
                rightUpCorner = Point(point.x, rightUpCorner.y)
            )
        })

        result.add(PointMover(
            points[7]
        )
        { point: Point ->
            this.copy(
                leftDownCorner = Point(leftDownCorner.x, point.y)
            )
        })


        return result
    }

    override fun getMovingPoints(): MutableList<Point> {
        return mutableListOf(
            leftDownCorner,
            leftUpCorner,
            rightUpCorner,
            rightDownCorner,
            Point(leftDownCorner.x, (leftDownCorner.y + leftUpCorner.y) / 2),
            Point((leftUpCorner.x + rightUpCorner.x) / 2, leftUpCorner.x),
            Point(rightUpCorner.x, (rightUpCorner.y + rightDownCorner.y) / 2),
            Point((leftDownCorner.x + rightDownCorner.x) / 2, rightDownCorner.y)
        )
    }


    override fun getDistanceToCountTouch(): Float {
        val dX = kotlin.math.abs(leftDownCorner.x - rightDownCorner.x)
        val dY = kotlin.math.abs(leftDownCorner.y - leftUpCorner.y)

        return max(min(dX, dY) / 4f, 20f)
    }


}


fun VertexFigureBuilder.buildRectangle(strokes: MutableList<Stroke>): Rectangle {
    val (maxX, maxY, minX, minY) = getStrokesRestrictions(strokes)
    return Rectangle(
        leftDownCorner = Point(minX, minY),
        rightUpCorner = Point(maxX, maxY)

    )
}

