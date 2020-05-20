package com.example.ochev.viewclasses.eventhandlers

import android.view.MotionEvent
import com.example.ochev.R
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.GraphDrawer
import com.example.ochev.viewclasses.StrokeDrawer

class EditingEventHandler(
    strokeDrawer: StrokeDrawer,
    graphDrawer: GraphDrawer,
    classifier: Classifier,
    vertexFigureEditor: VertexFigureEditor
) : GestureEventHandler(strokeDrawer, graphDrawer, classifier) {

    private var deleteButton = R.id.deleteButtonId

    override fun handle(gesture: Gesture, event: MotionEvent) {

    }
}