package com.example.ochev.viewclasses.eventhandlers

import android.app.Activity
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.core.graphics.alpha
import com.example.ochev.MainActivity
import com.example.ochev.R
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.GraphDrawer
import com.example.ochev.viewclasses.StrokeDrawer
import com.example.ochev.viewclasses.buttonshandler.ButtonsHandler

class EditingEventHandler(
    strokeDrawer: StrokeDrawer,
    graphDrawer: GraphDrawer,
    classifier: Classifier,
    vertexFigureEditor: VertexFigureEditor
) : GestureEventHandler(strokeDrawer, graphDrawer, classifier) {



    override fun handle(gesture: Gesture, event: MotionEvent) {



    }
}