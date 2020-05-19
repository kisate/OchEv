package com.example.ochev.viewclasses.eventhandlers

import android.view.MotionEvent
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.DrawGraphView
import com.example.ochev.viewclasses.DrawStrokeView

class EditingEventHandler(
    drawStrokeView: DrawStrokeView,
    drawGraphView: DrawGraphView,
    classifier: Classifier,
    vertexFigureEditor: VertexFigureEditor
) : GestureEventHandler(drawStrokeView, drawGraphView, classifier) {
    override fun handle(gestureType: GestureType?, event: MotionEvent): GestureType? {
        TODO("Not yet implemented")
    }
}