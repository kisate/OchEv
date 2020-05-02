package com.example.ochev.baseclasses.edgefigures

import com.example.ochev.baseclasses.EdgeFigure
import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.Point

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

}
