package com.example.ochev.baseclasses.vertexfigures.editors

import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.InformationForVertexEditor
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.PointInteractor

class VertexFigureRescaler(information: InformationForVertexEditor) {
    val figure: VertexFigure = information.figure
    lateinit var pointMovers: MutableList<PointMover>
    lateinit var currentMover: PointMover

    fun tryToStartMoving(point: Point): Boolean {
        pointMovers = figure.getPointMovers()
        val pointInteractor = PointInteractor()
        val closestMover = pointMovers.minBy { pointMover: PointMover ->
            pointInteractor.distance(point, pointMover.point)
        }!!

        return if (
            pointInteractor.distance(point, closestMover.point)
            <= figure.getDistanceToCountTouch()
        ) {
            currentMover = closestMover
            true
        } else {
            false
        }
    }

    fun newPoint(point: Point) {
        currentMover.moveFun(point)
    }


}