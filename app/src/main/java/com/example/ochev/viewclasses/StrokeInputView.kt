package com.example.ochev.viewclasses

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.buttonshandler.ButtonsHandler
import com.example.ochev.viewclasses.eventhandlers.GestureDetector
import com.example.ochev.viewclasses.eventhandlers.GestureHandler
import com.example.ochev.viewclasses.drawers.GraphDrawer
import com.example.ochev.viewclasses.drawers.LinesDrawer
import com.example.ochev.viewclasses.strokedrawers.StrokeDrawer


@SuppressLint("ViewConstructor")
class StrokeInputView(
    context: Context?,
    attrs: AttributeSet? = null,
    strokeDrawer: StrokeDrawer,
    graphDrawer: GraphDrawer,
    linesDrawer: LinesDrawer,
    buttonsHandler: ButtonsHandler,
    editText: SmartEditText,
    classifier: Classifier
) :
    View(context, attrs) {

    private val gestureDetector = GestureDetector()
    private val gestureHandler = GestureHandler(strokeDrawer, graphDrawer, linesDrawer, buttonsHandler, editText, classifier)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        Log.i("Touch", event.pointerCount.toString())

        Log.i("Touch", event.getPointerId(0).toString())

        gestureHandler.handle(gestureDetector.detect(event), event)

        return true
    }

}
