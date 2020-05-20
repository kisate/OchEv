package com.example.ochev.viewclasses.eventhandlers

import android.util.Log
import android.view.MotionEvent
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Vector
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.GraphDrawer
import com.example.ochev.viewclasses.StrokeDrawer
import kotlin.math.sqrt

class ScrollAndZoomEventHandler(
    strokeDrawer: StrokeDrawer,
    private val drawGraphView: GraphDrawer,
    classifier: Classifier
) : GestureEventHandler(strokeDrawer, drawGraphView, classifier) {

    private var lastCenter: Point? = null
    private var lastDistance: Float? = null
    private var firstPointerId: Int? = null
    private var secondPointerId: Int? = null

    override fun handle(gesture: Gesture, event: MotionEvent) {

        if (event.pointerCount == 2) {
            when (gesture.state) {
                GestureState.NONE -> {

                }
                GestureState.START -> {
                    lastCenter = calcCenter(event)
                    lastDistance = calcDistance(event)
                    firstPointerId = event.getPointerId(0)
                    secondPointerId = event.getPointerId(1)
                }
                GestureState.IN_PROGRESS -> {
                    drawGraphView.graphView.graphEditor.moveGraphByVector(
                        Vector(
                            lastCenter!!,
                            calcCenter(event)
                        )
                    )

                    val factor = calcDistance(event) / lastDistance!!

                    if (factor >= ZOOM_THRESHOLD && drawGraphView.graphView.scale*factor < MAX_SCALE)
                    {
                        drawGraphView.graphView.scale *= factor
                        drawGraphView.graphView.graphEditor.zoomByPointAndFactor(
                            calcCenter(event),
                            factor
                        )
                    }

                    if (factor <= 1/ ZOOM_THRESHOLD && drawGraphView.graphView.scale*factor > MIN_SCALE)
                    {
                        drawGraphView.graphView.scale *= factor
                        drawGraphView.graphView.graphEditor.zoomByPointAndFactor(
                            calcCenter(event),
                            factor
                        )
                    }

                    drawGraphView.graphView.invalidate()

                    lastDistance = calcDistance(event)
                    lastCenter = calcCenter(event)
                }
                GestureState.END -> {
                    lastCenter = null
                    lastDistance = null
                    firstPointerId = null
                    secondPointerId = null
                }
            }
        }
    }

    private fun calcCenter(event: MotionEvent): Point {

        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)

        return Point(x / 2, y / 2)
    }

    private fun calcDistance(event: MotionEvent): Float {

        return Point(
            event.getX(0),
            event.getY(0)
        ).getDistanceToPoint(Point(event.getX(1), event.getY(1)))
    }

    companion object {
        private const val ZOOM_THRESHOLD = 1.01f
        private const val MAX_SCALE = 20f
        private const val MIN_SCALE = 0.2f
    }
}