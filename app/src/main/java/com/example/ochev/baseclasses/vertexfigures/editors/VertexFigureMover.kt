package com.example.ochev.baseclasses.vertexfigures.editors

import com.example.ochev.baseclasses.dataclasses.InformationForVertexEditor
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Vector

class VertexFigureMover(information: InformationForVertexEditor) {
    val figure = information.figure
    lateinit var previous: Point

    fun tryToStartMove(point: Point): Boolean {
        if (
            figure.getDistanceToPoint(point) <= 100f ||
            figure.checkIfPointIsInside(point)
        ) {
            previous = point
            return true
        } else {
            return false
        }
    }

    fun newPoint(point: Point) {
        figure.moveByVector(Vector(previous, point))
        previous = point
    }
}