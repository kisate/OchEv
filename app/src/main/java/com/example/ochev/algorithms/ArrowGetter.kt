package com.example.ochev.algorithms

import android.graphics.Path
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Vector

class ArrowGetter {
    fun getPoints(from: Point, to: Point): MutableList<Point> {
        val height = 20f
        val base = 10f

        var reversed = Vector(to, from)
        reversed = reversed.multipliedByFloat((height) / reversed.length)
        var side = Vector(reversed.y, -reversed.x)
        val result: MutableList<Point> = ArrayList()
        side = side.multipliedByFloat(base / side.length)
        result.add(to.movedByVector(reversed.multipliedByFloat(1.3f)).movedByVector(side))
        side = side.multipliedByFloat(-1f)
        result.add(to.movedByVector(reversed.multipliedByFloat(1.3f)).movedByVector(side))
        result.add(to.movedByVector(reversed.multipliedByFloat(0.3f)))
        return result
    }

    fun getPathOfArrow(from: Point, to: Point): Path {
        val points = getPoints(from, to)
        val path = Path()
        path.moveTo(points.first().x, points.first().y)
        points.removeAt(0)
        path.lineTo(points.first().x, points.first().y)
        points.removeAt(0)
        path.lineTo(points.first().x, points.first().y)
        path.close()
        return path
    }
}