package com.example.ochev.viewclasses.eventhandlers

import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import com.example.ochev.MainActivity
import com.example.ochev.baseclasses.dataclasses.InformationForNormalizer
import com.example.ochev.ml.Classifier
import com.example.ochev.ml.Utils
import com.example.ochev.viewclasses.drawers.GraphDrawer
import com.example.ochev.viewclasses.strokedrawers.StrokeDrawer
import com.google.android.gms.tasks.Tasks
import java.util.concurrent.Callable

class DrawingEventHandler(
    private val strokeDrawer: StrokeDrawer,
    private val graphDrawer: GraphDrawer,
    private val classifier: Classifier
) : GestureEventHandler(strokeDrawer, graphDrawer, classifier) {

    override fun handle(gesture: Gesture, event: MotionEvent) {
        when (gesture.state) {
            GestureState.START -> {
                strokeDrawer.add(event.x, event.y)
            }
            GestureState.IN_PROGRESS -> {
                strokeDrawer.add(event.x, event.y)
            }
            GestureState.END -> {
                strokeDrawer.add(event.x, event.y)
                Log.i("ClassifyEdgeDbg", strokeDrawer.stroke.toString())
                classifyStroke()
                strokeDrawer.clear()
            }
            else -> {
            }
        }
    }

    private fun classifyStroke() {
        val bitmap = Utils.loadBitmapFromView(strokeDrawer.drawStrokeView)
        val information = InformationForNormalizer(
            classifier,
            bitmap,
            graphDrawer.graphEditor.graph,
            mutableListOf(strokeDrawer.stroke.copy())
        )
        Tasks.call(
            MainActivity.Executor.executorService,
            Callable {
                graphDrawer.modifyByStrokes(information)
            })
            .addOnSuccessListener {
                if (!it) Toast.makeText(graphDrawer.graphView.context, "Could not recognize.", Toast.LENGTH_LONG).show()
                graphDrawer.invalidate()
            }
            .addOnFailureListener { e -> Log.i("Modify", "Error modifying", e) }
    }
}