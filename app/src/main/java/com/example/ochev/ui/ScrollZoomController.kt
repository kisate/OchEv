package com.example.ochev.viewclasses.eventhandlers

import android.util.Log
import android.view.MotionEvent
import com.example.ochev.Utils.Provider
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Vector
import com.example.ochev.baseclasses.editors.boardeditor.BoardViewer
import com.example.ochev.ui.Gesture
import com.example.ochev.ui.GestureState
import com.example.ochev.ui.GestureType

class ScrollZoomController(
    private val viewerProvider: Provider<BoardViewer?>
) {
    private var lastCenter: Point? = null
    private var lastDistance: Float? = null
    private var firstPointerId: Int? = null
    private var secondPointerId: Int? = null

    /**
     * TODO: add this logic in board viewer
     */
    private var currentScale: Float = 1f

    fun handle(gesture: Gesture, event: MotionEvent) {
        if (gesture.type != GestureType.SCROLL_AND_ZOOM) {
            return
        }
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
                    viewerProvider.get()?.moveBoard(
                        Vector(
                            lastCenter ?: return,
                            calcCenter(event)
                        )
                    )

                    val factor = calcDistance(event) / (lastDistance ?: return)


                    if (factor >= ZOOM_THRESHOLD && currentScale * factor < MAX_SCALE) {
                        currentScale *= factor
                        viewerProvider.get()?.scaleBoard(
                            calcCenter(event),
                            factor
                        )
                    }

                    if (factor <= 1 / ZOOM_THRESHOLD && currentScale * factor > MIN_SCALE) {
                        currentScale *= factor
                        viewerProvider.get()?.scaleBoard(
                            calcCenter(event),
                            factor
                        )
                    }

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
        private const val ZOOM_THRESHOLD = 1.00001f
        private const val MAX_SCALE = 20f
        private const val MIN_SCALE = 0.01f
    }
}