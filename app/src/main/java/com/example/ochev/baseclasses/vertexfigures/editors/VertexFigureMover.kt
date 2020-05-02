package com.example.ochev.baseclasses.vertexfigures.editors

import com.example.ochev.baseclasses.dataclasses.InformationForVertexEditor
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Vector

class VertexFigureMover(information: InformationForVertexEditor) {
    val figure = information.figure
    lateinit var previous: Point

    fun tryToStartMove(point: Point): Boolean {
        return if (figure.getDistanceToPointOrZeroIfInside(point) <= 100f) {
            previous = point
            true
        } else {
            false
        }
    }

    fun newPoint(point: Point) {
        figure.moveByVector(Vector(previous, point))
        previous = point
    }
}