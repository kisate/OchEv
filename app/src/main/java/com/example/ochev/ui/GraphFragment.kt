package com.example.ochev.ui

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.example.ochev.R
import com.example.ochev.baseclasses.editors.boardeditor.BoardViewer
import com.example.ochev.ml.Utils

class GraphFragment : Fragment() {
    private var container: RelativeLayout? = null
    private var inputView: InputView? = null
    private var inputDrawView: InputDrawView? = null
    private var figureDrawingView: FigureDrawingView? = null

    private var inputStrokeHandler: InputStrokeHandler? = null
    private var sideEnvironmentSettingsController: SideEnvironmentSettingsController? = null
    private val viewer: BoardViewer?
        get() {
            val args = arguments ?: return null
            return ApplicationComponent.viewersHolder.getViewer(args.getString("id", "-1"))
        }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "view created")
        this.container = inflater.inflate(R.layout.graph_fragment_view, container, true)
            .findViewById(R.id.graph_fragment_view)
        initializeInputViews()
        initializeSideEnvironmentSettings()
        initializeDrawers()
        return this.container
    }

    private fun initializeDrawers() {
        val container = container ?: return
        val viewer = viewer ?: return
        figureDrawingView = container.findViewById(R.id.figure_drawing_view)
        figureDrawingView?.paint?.let {
            it.style = Paint.Style.STROKE
            it.strokeWidth = 3f
        }
        viewer.addBoardChangesListener {
            figureDrawingView?.figures = it
        }
    }

    private fun initializeInputViews() {
        val container = this.container ?: return

        inputDrawView = container.findViewById(R.id.input_draw_view)

        inputDrawView?.paint?.let {
            it.style = Paint.Style.STROKE
            it.strokeWidth = 2.5f
        }
        val detector = GestureDetector()
        inputView = container.findViewById(R.id.input_view)
        inputView?.setOnTouchEventListener { event ->
            sideEnvironmentSettingsController?.hideSettings()

            Log.d(TAG, "touched: $event")
            val gesture = detector.detect(event)
            Log.d(TAG, "gesture is $gesture")
            return@setOnTouchEventListener when (gesture.type) {
                GestureType.MOVE -> handleMove(event, gesture)
                else -> true
            }
        }
    }

    private fun initializeSideEnvironmentSettings() {
        val container = container ?: return
        val settingsView =
            container.findViewById<LinearLayout>(R.id.side_environment_settings_container)
        val enterPoint =
            container.findViewById<FrameLayout>(R.id.enter_side_environment_settings)

        sideEnvironmentSettingsController = SideEnvironmentSettingsController(
            settingsView,
            enterPoint,
        )
        sideEnvironmentSettingsController?.initialize()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "created")
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
                val inputDrawView = inputDrawView ?: return false
                val handler = inputStrokeHandler ?: return false
                val bitmap = Utils.loadBitmapFromView(inputDrawView) ?: return false
                val viewer = viewer ?: return false
                viewer.createFigureByStrokes(
                    bitmap,
                    mutableListOf(handler.stroke)
                )
                inputDrawView.clear()
                inputStrokeHandler = null
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        container = null
        inputView = null
        inputDrawView = null
        figureDrawingView = null
    }

    companion object {
        private val TAG = "GraphFragment"

        fun newInstance(id: String): GraphFragment {
            val args = Bundle()
            args.putString("id", id)
            val fragment = GraphFragment()
            fragment.arguments = args
            return fragment
        }
    }
}