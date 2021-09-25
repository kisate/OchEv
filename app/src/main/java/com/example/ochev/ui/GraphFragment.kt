package com.example.ochev.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.ochev.R
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor
import com.example.ochev.ml.Classifier

class GraphFragment(
    private val graphEditor: GraphEditor,
) : Fragment() {
    private var container: FrameLayout? = null
    private var inputView: InputView? = null
    private var inputDrawView: InputDrawView? = null
    private var classifier: Classifier? = null

    private var inputStrokeHandler: InputStrokeHandler? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "view created")
        this.container = inflater.inflate(R.layout.graph_fragment_view, container, true)
            .findViewById(R.id.graph_fragment_view)
        inputDrawView = this.container?.findViewById(R.id.input_draw_view)

        val paint = inputDrawView?.paint
        if (paint != null) {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 2.5f
        }
        val detector = GestureDetector()
        inputView = this.container?.findViewById(R.id.input_view)
        inputView?.setOnTouchEventListener { event ->
            Log.d(TAG, "touched: $event")
            val gesture = detector.detect(event)
            Log.d(TAG, "gesture is $gesture")
            return@setOnTouchEventListener when (gesture.type) {
                GestureType.MOVE -> handleMove(event, gesture)
                else -> true
            }
        }
        return this.container
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "created")
        classifier = Classifier(requireContext())
    }

    private fun handleMove(event: MotionEvent, gesture: Gesture): Boolean {
        Log.d(TAG, "handling move: $event $gesture")
        return when (gesture.state) {
            GestureState.NONE -> true
            GestureState.START -> {
                inputStrokeHandler = InputStrokeHandler(inputDrawView)
                inputStrokeHandler?.onStart(event)

                true
            }
            GestureState.IN_PROGRESS -> {
                inputStrokeHandler?.onContinue(event)
                true
            }
            GestureState.END -> {
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        container = null
        inputView = null
        inputDrawView = null
    }

    companion object {
        private val TAG = "GraphFragment"
    }
}