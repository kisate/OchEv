package com.example.ochev.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.ochev.R
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.editors.boardeditor.BoardManipulator
import com.example.ochev.baseclasses.editors.boardeditor.BoardViewer
import com.example.ochev.ml.Utils
import com.example.ochev.viewclasses.eventhandlers.ScrollZoomController


class GraphFragment : Fragment() {
    private var container: RelativeLayout? = null
    private var inputView: InputView? = null
    private var inputDrawView: InputDrawView? = null
    private var figureDrawingView: FigureDrawingView? = null
    private var currentManipulator: BoardManipulator? = null
        set(value) {
            field = value
            figureDrawingView?.invalidate()
        }

    private var inputStrokeHandler: InputStrokeHandler? = null
    private var sideEnvironmentSettingsController: SideEnvironmentSettingsController? = null
    private var editingButtonsController: EditingButtonsController? = null
    private var historyButtonsController: HistoryButtonsController? = null
    private var scrollZoomController: ScrollZoomController? = null

    private val viewer: BoardViewer?
        get() {
            val id = getGraphId() ?: return null
            return ApplicationComponent.viewersHolder.getViewer(id)
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
        initializeEditingMode()
        initializeHistory()
        initializeScrollZoom()
        initializeViewer()
        initializeHelpers()
        initializeFontListener()
        initializeEditTextDialog()
        return this.container
    }

    private fun initializeEditTextDialog() {
        viewer?.addStartTextEditingListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            val input = EditText(context)
            val beginText = currentManipulator?.getCurrentText() ?: ""
            input.inputType = InputType.TYPE_CLASS_TEXT
            input.setText(beginText, TextView.BufferType.EDITABLE)
            input.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        s?.let { currentManipulator?.putText(it.toString()) }
                    }

                    override fun afterTextChanged(s: Editable?) = Unit
                }
            )
            builder.setView(input)
            builder.setPositiveButton("Save") { dialog, which ->
                currentManipulator?.putText(input.text.toString())
                dialog.cancel()
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                currentManipulator?.putText(beginText)
                dialog.cancel()
            }

            builder.show()
        }
    }

    private fun initializeFontListener() {
        viewer?.addFontSizeListener {
            editingButtonsController?.setSeekBarProgress(it.toInt())
        }
    }

    private fun initializeHelpers() {
        viewer?.addSuggestLineChangesListenerAndNotify {
            Log.d(TAG, "receive ${it.size} suggests for draw")
            figureDrawingView?.suggests = it
        }
        figureDrawingView?.paintSuggests?.let {
            it.style = Paint.Style.STROKE
            it.color = Color.parseColor("#FFD300")
            it.strokeWidth = 3f
            it.isAntiAlias = true
        }
    }

    private fun initializeViewer() {
        val checkedViewer = viewer ?: return
        checkedViewer.setLastEnterTimeMs(System.currentTimeMillis())

        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        checkedViewer.setWindowParams(displayMetrics.heightPixels, displayMetrics.widthPixels)
    }

    private fun initializeScrollZoom() {
        scrollZoomController = ScrollZoomController { viewer }
    }

    private fun initializeHistory() {
        val container = this.container ?: return
        val view = container.findViewById<ConstraintLayout>(R.id.history_buttons_container)
        historyButtonsController = HistoryButtonsController(view) { viewer }
        viewer?.addRedoChangeShowButtonListenerAndNotify {
            if (it) {
                historyButtonsController?.showForward(true)
            } else {
                historyButtonsController?.hideForward(true)
            }
        }
        viewer?.addUndoChangeShowButtonListenerAndNotify {
            if (it) {
                historyButtonsController?.showUndo(true)
            } else {
                historyButtonsController?.hideUndo(true)
            }
        }
    }

    private fun initializeEditingMode() {
        val container = this.container ?: return
        val view = container.findViewById<ConstraintLayout>(R.id.editing_buttons_container)
        editingButtonsController = EditingButtonsController(view) { currentManipulator }
        viewer?.addUserModeChangesListenerAndNotify {
            if (it.isEditing) {
                editingButtonsController?.show(it, true)
            } else {
                currentManipulator = null
                editingButtonsController?.hide(true)
            }
        }
    }

    private fun initializeDrawers() {
        val container = container ?: return
        val viewer = viewer ?: return
        figureDrawingView = container.findViewById(R.id.figure_drawing_view)
        figureDrawingView?.paintStroke?.let {
            it.style = Paint.Style.STROKE
            it.strokeWidth = 3f
            it.isAntiAlias = true
        }
        figureDrawingView?.paintFill?.let {
            it.style = Paint.Style.FILL
            it.setColor(context?.getColor(R.color.white) ?: return)
            it.isAntiAlias = true
        }
        viewer.addBoardChangesListenerAndNotify {
            figureDrawingView?.figures = it
        }
        figureDrawingView?.setProvider {
            currentManipulator?.getId()
        }
        figureDrawingView?.setManipulatorProvider {
            currentManipulator
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


            Log.d(TAG, "touched: $event")
            val gesture = detector.detect(event)
            Log.d(TAG, "gesture is $gesture")
            return@setOnTouchEventListener when (gesture.type) {
                GestureType.TAP -> handleTap(event, gesture)
                GestureType.MOVE -> handleMove(event, gesture)
                GestureType.SCROLL_AND_ZOOM -> {
                    scrollZoomController?.handle(gesture, event)
                    true
                }
                GestureType.LONG_TAP -> handleLongTap(event, gesture)
                else -> true
            }
        }
    }

    private fun initializeSideEnvironmentSettings() {
        val container = container ?: return
        val settingsView =
            container.findViewById<LinearLayout>(R.id.side_environment_settings_container)
        val enterPoint =
            container.findViewById<ImageView>(R.id.enter_side_environment_settings)

        sideEnvironmentSettingsController =
            SideEnvironmentSettingsController(
                settingsView,
                enterPoint,
                { viewer },
                { getGraphId() },
                { saveBitmap() })
        sideEnvironmentSettingsController?.initialize()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "created")
    }

    override fun onPause() {
        super.onPause()
        saveBitmap()
    }

    private fun saveBitmap() {
        viewer?.setGraphBitmap(Utils.loadBitmapFromView(figureDrawingView ?: return) ?: return)
    }

    private fun handleTap(event: MotionEvent, gesture: Gesture): Boolean {
        Log.d(TAG, "handling tap: $event $gesture")
        return when (gesture.state) {
            GestureState.NONE -> true
            GestureState.START -> true
            GestureState.IN_PROGRESS -> true
            GestureState.END -> {
                if (
                    sideEnvironmentSettingsController?.isShown() == true &&
                    currentManipulator != null
                ) {
                    sideEnvironmentSettingsController?.hideSettings(true)
                    return true
                }
                currentManipulator = viewer?.selectFigureByPoint(Point(event))
                sideEnvironmentSettingsController?.hideSettings(true)
                true
            }
        }
    }

    private fun handleLongTap(event: MotionEvent, gesture: Gesture): Boolean {
        return when (gesture.state) {
            GestureState.NONE -> true
            GestureState.IN_PROGRESS -> true
            GestureState.START -> true
            GestureState.END -> {
                currentManipulator = viewer?.selectFigureByPoint(Point(event))
                currentManipulator?.startEditingText()
                return true
            }
        }
    }

    private fun getGraphId(): String? {
        val args = arguments ?: return null
        return args.getString("id")
    }

    private fun handleMove(event: MotionEvent, gesture: Gesture): Boolean {
        Log.d(TAG, "handling move: $event $gesture")
        val manipulator = currentManipulator
        return when (gesture.state) {
            GestureState.NONE -> true
            GestureState.START -> {
                sideEnvironmentSettingsController?.hideSettings(true)
                if (manipulator != null) {
                    manipulator.startEditing(Point(event))
                    currentManipulator = manipulator.putPoint(Point(event))
                    if (currentManipulator == null) {
                        inputStrokeHandler = InputStrokeHandler(inputDrawView)
                        inputStrokeHandler?.onStart(event)
                    }
                } else {
                    inputStrokeHandler = InputStrokeHandler(inputDrawView)
                    inputStrokeHandler?.onStart(event)
                }
                true
            }
            GestureState.IN_PROGRESS -> {
                if (manipulator != null) {
                    currentManipulator = manipulator.putPoint(Point(event))
                } else {
                    inputStrokeHandler?.onContinue(event)
                }
                true
            }
            GestureState.END -> {
                if (manipulator != null) {
                    currentManipulator = manipulator.putPoint(Point(event))
                    currentManipulator?.cancelEditing(Point(event))
                    return true
                }

                val inputDrawView = inputDrawView ?: return false
                val handler = inputStrokeHandler ?: return false
                val viewer = viewer ?: return false
                val bitmap = Utils.loadBitmapFromView(inputDrawView) ?: return false
                val deepcopy = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                viewer.createFigureByStrokes(
                    deepcopy,
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
        currentManipulator = null
        editingButtonsController?.onDestroy()
        editingButtonsController = null
        sideEnvironmentSettingsController?.onDestroy()
        sideEnvironmentSettingsController = null
        historyButtonsController = null
        scrollZoomController = null
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