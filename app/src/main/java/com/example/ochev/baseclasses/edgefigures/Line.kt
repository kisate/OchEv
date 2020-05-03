package com.example.ochev.baseclasses.edgefigures

import com.example.ochev.baseclasses.EdgeFigure
import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.PointInteractor

class Line(
    val beginFigure: VertexFigure,
    val endFigure: VertexFigure
) : EdgeFigure() {
    override fun getDistanceToPoint(point: Point): Float {
        return point.getDistanceToLineSegment(
            beginFigure.center,
            endFigure.center
        )
    }

    override fun getDistanceToCountTouch(): Float {
        val pointInteractor = PointInteractor()
        return pointInteractor.distance(beginFigure.center, endFigure.center) / 4f
    }

}
