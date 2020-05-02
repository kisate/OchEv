package com.example.ochev.baseclasses.vertexfigures.editors

import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.InformationForVertexEditor
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.PointInteractor

class VertexFigureRescaler(information: InformationForVertexEditor) {
    val figure: VertexFigure = information.figure
    var pointMovers = figure.getPointMovers()
    lateinit var currentMover: PointMover

    fun tryToStartMoving(point: Point): Boolean {
        pointMovers = figure.getPointMovers()
        val pointInteractor = PointInteractor()
        val closestMover = pointMovers.minBy { pointMover: PointMover ->
            pointInteractor.distance(point, pointMover.point)
        }!!

        return if (pointInteractor.distance(point, closestMover.point) > 100f) false
        else {
            currentMover = closestMover
            true
        }
    }

    fun newPoint(point: Point) {
        currentMover.moveFun(point)
    }


}