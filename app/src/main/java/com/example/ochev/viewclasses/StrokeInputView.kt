package com.example.ochev.viewclasses

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.ochev.MainActivity
import com.example.ochev.baseclasses.EdgeFigure
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.*
import com.example.ochev.baseclasses.timeinteractors.Throttle
import com.example.ochev.baseclasses.vertexfigures.editors.VertexFigureEditor
import com.example.ochev.ml.Classifier
import com.example.ochev.ml.Utils
import com.google.android.gms.tasks.Tasks
import java.util.concurrent.Callable

enum class InputMode(value: Int) {
    DRAWING(1),
    EDITING(2),
    MOVING_ALL(3);
}

@SuppressLint("ViewConstructor")
class StrokeInputView(
    context: Context?,
    attrs: AttributeSet? = null,
    drawStrokeView: DrawStrokeView,
    drawFiguresView: DrawGraphView,
    classifier: Classifier
) :
    View(context, attrs) {

    private val inputHandler = InputHandler(this, drawStrokeView, drawFiguresView, classifier)
    private val throttle = Throttle(2)
    private val funMap = HashMap<InputMode, HashMap<Int, (InputHandler, MotionEvent) -> Unit>>()

    var inputMode = InputMode.DRAWING

    fun clear() {
        inputHandler.clear()
    }

    private fun addToFunMap(
        mode: InputMode,
        eventAction: Int,
        function: (InputHandler, MotionEvent) -> Unit
    ) {
        if (!funMap.containsKey(mode)) funMap[mode] = HashMap()
        funMap[mode]!![eventAction] = function
    }

    init {
        addToFunMap(InputMode.DRAWING, MotionEvent.ACTION_DOWN) { inputHandler, event ->
            inputHandler.touchStart(event)
        }
        addToFunMap(InputMode.DRAWING, MotionEvent.ACTION_MOVE) { inputHandler, event ->
            inputHandler.touchMove(event)
        }
        addToFunMap(InputMode.DRAWING, MotionEvent.ACTION_UP) { inputHandler, event ->
            inputHandler.touchUp(event)
        }

        addToFunMap(InputMode.EDITING, MotionEvent.ACTION_DOWN) { inputHandler, event ->
            inputHandler.movementStart(event)
        }
        addToFunMap(InputMode.EDITING, MotionEvent.ACTION_MOVE) { inputHandler, event ->
            inputHandler.movementMove(event)
        }
        addToFunMap(InputMode.EDITING, MotionEvent.ACTION_UP) { inputHandler, event ->
            inputHandler.movementUp(event)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var currentEvent: MotionEvent? = null
        throttle.attempt(Runnable { currentEvent = event })

        Log.i("Touch", event.pointerCount.toString())

        event?.let { (funMap[inputMode]!![event.action])?.let { it1 -> it1(inputHandler, it) } }

        return true
    }
}

enum class MovementType(value: Int) {
    CHILL(0),
    MOVING(1),
    RESCALING(2);
}

class InputHandler(
    private val strokeInputView: StrokeInputView,
    private val drawStrokeView: DrawStrokeView,
    private val drawGraphView: DrawGraphView,
    private val classifier: Classifier
) {
    private var drawStrokeInteractor = drawStrokeView.drawStrokeInteractor
    private var stroke: Stroke = Stroke()
    private lateinit var firstPoint: Point
    private lateinit var lastPoint: Point
    private lateinit var lastEditingFigure: Figure

    private var vertexFigureEditor: VertexFigureEditor? = null
    private var movementType = MovementType.CHILL

    private var lastTime = 0L
    private var ACCURACY = 15f // radius of checking unnecessary movement while drawing stroke
        set(value) {
            field = value
        }
    private var EDITMODEDURATION = 300 // time between touchdown and touchup (in ms)
        set(value) {
            field = value
        }
    private var EDITMODEACCURACY =
        50f // radius of checking unnecessary movement while checking entry to edit mode
        set(value) {
            field = value
        }


    fun touchMove(event: MotionEvent?) {
        val point = event?.let { Point(it) } ?: return
        if (PointInteractor().distance(point, lastPoint) <= ACCURACY) return
        stroke.addPoint(point)
        drawStrokeInteractor.set(drawStrokeView, stroke)
        lastPoint = point
    }

    fun touchUp(event: MotionEvent?) {

        val point = event?.let { Point(it) }

        if (point != null) {
            lastPoint = point
        }

        if (possibleEditModeEntry() && checkEditModeEntry()) {
            Log.i("timeDebug", (System.nanoTime() - lastTime).toString())
            enterEditing(drawGraphView.graph.getFigureForEditing(lastPoint)!!)

            if (lastEditingFigure is VertexFigure) {
                vertexFigureEditor =
                    VertexFigureEditor(InformationForVertexEditor(lastEditingFigure as VertexFigure))
            } else if (lastEditingFigure is EdgeFigure) {
                vertexFigureEditor = null
            }

            drawGraphView.invalidate()
        } else {
            classifyStroke()
        }
        stroke = Stroke()
        drawStrokeInteractor.clear(drawStrokeView)
    }

    fun touchStart(event: MotionEvent?) {
        val point = event?.let { Point(it) } ?: return
        firstPoint = point
        lastTime = System.nanoTime()
        lastPoint = point
        stroke.addPoint(point)
        drawStrokeInteractor.set(drawStrokeView, stroke)
    }

    fun possibleEditModeEntry(): Boolean {
        return (System.nanoTime() - lastTime) / MICROSECOND <= EDITMODEDURATION && PointInteractor().distance(
            lastPoint,
            firstPoint
        ) <= EDITMODEACCURACY
    }

    fun enterEditing(figure: Figure) {
        if (figure is VertexFigure) {
            figure.heightOnPlain = drawGraphView.graph.maximalHeight + 1
        }
        lastEditingFigure = figure
        figure.drawingInformation.set(DrawingMode.EDIT)
        strokeInputView.inputMode = InputMode.EDITING
    }

    fun closeEditing(figure: Figure) {
        figure.drawingInformation.set(DrawingMode.DEFAULT)
        strokeInputView.inputMode = InputMode.DRAWING
    }

    fun checkEditModeEntry(): Boolean {
        return drawGraphView.graph.getFigureForEditing(firstPoint) != null && drawGraphView.graph.getFigureForEditing(
            lastPoint
        ) != null
    }

    fun checkMovementModeEntry(): Boolean {
        return false
    }

    fun classifyStroke() {
        val bitmap = Utils.loadBitmapFromView(drawStrokeView)

        val information = InformationForNormalizer(
            classifier,
            bitmap,
            drawGraphView.graph,
            mutableListOf(stroke.copy())
        )

        Tasks.call(
            MainActivity.Executor.executorService,
            Callable<Figure?> {
                drawGraphView.graph.modifyByStrokes(information)
            })
            .addOnSuccessListener { figure ->
                Log.i("Modify", "Classified as $figure")
                drawGraphView.invalidate()
                if (figure == null)
                    Toast.makeText(drawGraphView.context, "Could not recognize", Toast.LENGTH_SHORT)
                        .show()
            }
            .addOnFailureListener { e -> Log.i("Modify", "Error modifying", e) }

        Log.i("dbgCountOfPointInStroke", stroke.points.size.toString())
    }

    fun clear() {
        drawGraphView.clear()
        drawStrokeInteractor.clear(drawStrokeView)
    }

    fun movementStart(event: MotionEvent?) {
        val point = event?.let { Point(it) }
        movementType = MovementType.CHILL
        if (point != null) {
            if (vertexFigureEditor != null) {
                if (vertexFigureEditor!!.rescaler.tryToStartMoving(point)) {
                    movementType = MovementType.RESCALING
                } else if (vertexFigureEditor!!.mover.tryToStartMove(point)) {
                    movementType = MovementType.MOVING
                }
            }
        }
    }

    fun movementMove(event: MotionEvent?) {
        val point = event?.let { Point(it) } ?: return
        when (movementType) {
            MovementType.MOVING -> {
                vertexFigureEditor!!.mover.newPoint(point)
            }
            MovementType.RESCALING -> {
                vertexFigureEditor!!.rescaler.newPoint(point)
            }
            else -> {
                return
            }
        }
        drawGraphView.invalidate()
    }

    fun movementUp(event: MotionEvent?) {
        val point = event?.let { Point(it) } ?: return
        when (movementType) {
            MovementType.MOVING -> {
                vertexFigureEditor!!.mover.newPoint(point)
            }
            MovementType.RESCALING -> {
                vertexFigureEditor!!.rescaler.newPoint(point)
            }
            MovementType.CHILL -> {
                closeEditing(lastEditingFigure)
            }
        }
        drawGraphView.invalidate()
    }

    companion object {
        private const val MICROSECOND = 1000000f // nanosecond / microsecond = milisecond
//        private const val TIMEBEFOREMOVEMENT =
    }
}