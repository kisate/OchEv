package com.example.ochev.viewclasses.eventhandlers

import android.view.MotionEvent
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.DrawGraphView
import com.example.ochev.viewclasses.DrawStrokeView
import com.example.ochev.viewclasses.StrokeDrawer

class EditingEventHandler(
    strokeDrawer: StrokeDrawer,
    drawGraphView: DrawGraphView,
    classifier: Classifier,
    vertexFigureEditor: VertexFigureEditor
) : GestureEventHandler(strokeDrawer, drawGraphView, classifier) {
    override fun handle(gesture: Gesture, event: MotionEvent) {
        //TODO("123")
    }
}