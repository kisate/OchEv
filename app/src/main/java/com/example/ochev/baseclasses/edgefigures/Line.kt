package com.example.ochev.baseclasses.edgefigures

import com.example.ochev.baseclasses.EdgeFigure
import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.Point

class Line(
    beginFigure: VertexFigure,
    endFigure: VertexFigure
) : EdgeFigure(beginFigure, endFigure) {
    override fun getDistanceToPoint(point: Point): Float {
        return point.getDistanceToLineSegment(
            beginFigure.center,
            endFigure.center
        )
    }

    override fun getDistanceToCountTouch(): Float {
        return beginFigure.center.getDistanceToPoint(endFigure.center) / 4f
    }

}
