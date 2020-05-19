package com.example.ochev.viewclasses

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.ochev.MainActivity
import com.example.ochev.baseclasses.EdgeFigure
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.*
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.baseclasses.timeinteractors.Throttle
import com.example.ochev.ml.Classifier
import com.example.ochev.ml.Utils
import com.example.ochev.viewclasses.eventhandlers.DefaultInputHandler
import com.example.ochev.viewclasses.eventhandlers.GestureDetector
import com.example.ochev.viewclasses.eventhandlers.GestureHandler
import com.example.ochev.viewclasses.eventhandlers.GestureType
import com.google.android.gms.tasks.Tasks
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.concurrent.Callable

enum class InputMode(value: Int) {
    DEFAULT(0),
    DRAWING(1),
    EDITING(2),
    SCROLLING(3);
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

    private val gestureDetector = GestureDetector()
    private val gestureHandler = GestureHandler(drawStrokeView, drawFiguresView, classifier)

    fun clear() {
        inputHandler.clear()
    }

    fun deleteEditingFigure() {
        inputHandler.deleteEditingFigure()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        Log.i("Touch", event.pointerCount.toString())

        Log.i("Touch", event.getPointerId(0).toString())

        gestureHandler.handle(gestureDetector.detect(event), event)

        return true
    }

    fun addDeleteButton(deleteButtonId: Button?) {
        inputHandler.deleteButton = deleteButtonId
    }
}

enum class MovementType(value: Int) {
    CHILL(0),
    MOVING(1),
    RESCALING(2);
}

class InputHandler(
    private val drawStrokeView: DrawStrokeView,
    private val drawGraphView: DrawGraphView,
    private val classifier: Classifier
) {
    private var drawStrokeInteractor = drawStrokeView.drawStrokeInteractor
    private var stroke: Stroke = Stroke()
    private val funMap = HashMap<InputMode, HashMap<Int, (MotionEvent) -> Unit>>()
    private var inputMode = InputMode.DEFAULT
        set(value) {
            Log.i("InputMode", "set to $value")
            field = value
        }
    var deleteButton: Button? = null

    private lateinit var firstPoint: Point
    private lateinit var lastPoint: Point
    private lateinit var lastEditingFigure: Figure

    private var vertexFigureEditor: VertexFigureEditor? = null
    private var movementType = MovementType.CHILL

    private lateinit var lastScrollCenter: Point

    private var lastTime = 0L
    private var ACCURACY = 15f // radius of checking unnecessary movement while drawing stroke
        set(value) {
            field = value
        }
    private var EDIT_MODE_DURATION = 300 // time between touchdown and touchup (in ms)
        set(value) {
            field = value
        }
    private var EDIT_MODE_ACCURACY =
        50f // radius of checking unnecessary movement while checking entry to edit mode
        set(value) {
            field = value
        }

    init {
        addToFunMap(InputMode.DRAWING, MotionEvent.ACTION_MOVE) { event ->
            drawMove(event)
        }
        addToFunMap(InputMode.DRAWING, MotionEvent.ACTION_UP) { event ->
            drawUp(event)
        }

        addToFunMap(InputMode.EDITING, MotionEvent.ACTION_DOWN) { event ->
            movementStart(event)
        }
        addToFunMap(InputMode.EDITING, MotionEvent.ACTION_MOVE) { event ->
            movementMove(event)
        }
        addToFunMap(InputMode.EDITING, MotionEvent.ACTION_UP) { event ->
            movementUp(event)
        }

        addToFunMap(InputMode.SCROLLING, MotionEvent.ACTION_MOVE) { event ->
            scrollingMove(event)
        }
        addToFunMap(InputMode.SCROLLING, MotionEvent.ACTION_UP) { event ->
            scrollingUp(event)
        }
    }

    private fun addToFunMap(
        mode: InputMode,
        eventAction: Int,
        function: (MotionEvent) -> Unit
    ) {
        if (!funMap.containsKey(mode)) funMap[mode] = HashMap()
        funMap[mode]!![eventAction] = function
    }

    private fun drawStart(event: MotionEvent?) {
        val point = event?.let { Point(it) } ?: return
        inputMode = InputMode.DRAWING
        drawStrokeInteractor.set(drawStrokeView, stroke)
    }

    private fun drawMove(event: MotionEvent?) {
        val point = event?.let { Point(it) } ?: return
        if (PointInteractor().distance(point, lastPoint) <= ACCURACY) return
        drawStrokeInteractor.set(drawStrokeView, stroke)
    }

    private fun drawUp(event: MotionEvent?) {
        val point = event?.let { Point(it) }
        classifyStroke()
        inputMode = InputMode.DEFAULT
    }

    fun touchMove(event: MotionEvent?) {
        val point = event?.let { Point(it) } ?: return
        if (PointInteractor().distance(point, lastPoint) <= ACCURACY) return

        if (event.pointerCount == 1) stroke.addPoint(point)
        if (inputMode != InputMode.DEFAULT) {
            funMap[inputMode]!![event.action]?.let { it(event) }
        } else if (checkScrollingModeEntry(event)) {
            enterScrolling(event)
        } else if (!possibleEditModeEntry()) {
            drawStart(event)
        }
        lastPoint = point
    }

    fun touchUp(event: MotionEvent?) {
        if (event != null) {
            val point = Point(event)
            lastPoint = point
            if (inputMode == InputMode.DEFAULT && possibleEditModeEntry() && checkEditModeEntry()) {
                Log.i("timeDebug", (System.nanoTime() - lastTime).toString())
                startEditing()

            } else if (inputMode != InputMode.DEFAULT) {
                funMap[inputMode]!![event.action]?.let { it(event) }
            }
        }
        stroke = Stroke()
        drawStrokeInteractor.clear(drawStrokeView)
    }

    fun touchDown(event: MotionEvent?) {
        val point = event?.let { Point(it) } ?: return
        firstPoint = point
        lastTime = System.nanoTime()
        stroke.addPoint(point)

        if (inputMode != InputMode.DEFAULT) {
            funMap[inputMode]!![event.action]?.let { it(event) }
        }

        lastPoint = point

    }

    private fun possibleEditModeEntry(): Boolean {
        return (System.nanoTime() - lastTime) / MICROSECOND <= EDIT_MODE_DURATION && PointInteractor().distance(
            lastPoint,
            firstPoint
        ) <= EDIT_MODE_ACCURACY
    }

    private fun startEditing() {
        inputMode = InputMode.EDITING
        if (deleteButton != null){
            deleteButton!!.alpha = 1f
        }
        enterEditing(drawGraphView.graph.getFigureForEditing(lastPoint)!!)
        if (lastEditingFigure is VertexFigure) {
            vertexFigureEditor =
                VertexFigureEditor(
                    InformationForVertexEditor(lastEditingFigure as VertexFigure)
                )
        } else if (lastEditingFigure is EdgeFigure) {
            vertexFigureEditor = null
        }
        drawGraphView.invalidate()
    }

    private fun enterEditing(figure: Figure) {
        if (figure is VertexFigure) {
            figure.heightOnPlain = drawGraphView.graph.maximalHeight + 1
        }
        lastEditingFigure = figure
        figure.drawingInformation.set(DrawingMode.EDIT)

    }

    private fun closeEditing(figure: Figure) {
        figure.drawingInformation.set(DrawingMode.DEFAULT)
        inputMode = InputMode.DEFAULT
        if (deleteButton != null){
            deleteButton!!.alpha = 0f
        }
    }

    private fun checkEditModeEntry(): Boolean {
        return drawGraphView.graph.getFigureForEditing(firstPoint) != null && drawGraphView.graph.getFigureForEditing(
            lastPoint
        ) != null
    }

    private fun enterScrolling(event: MotionEvent) {
        inputMode = InputMode.SCROLLING
        scrollingStart(event)
    }

    private fun checkScrollingModeEntry(event: MotionEvent?): Boolean {
        if (event == null) return false
        return (System.nanoTime() - lastTime) / MICROSECOND < SCROLLING_THRESHOLD && event.pointerCount == 2
    }

    private fun classifyStroke() {
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
        inputMode = InputMode.DEFAULT
    }

    private fun scrollingStart(event: MotionEvent?) {
        if (event == null) return
        val firstFinger = Point(event.getX(0).toInt(), event.getY(0).toInt())
        val secondFinger = Point(event.getX(1).toInt(), event.getY(1).toInt())
        lastScrollCenter = PointInteractor.centerOfMass(arrayOf(firstFinger, secondFinger))
    }

    private fun scrollingMove(event: MotionEvent?) {
        if (event == null) return
        if (event.pointerCount < 2) {
            scrollingUp(event)
            return
        }

        val point = Point(event)

        val firstFinger = Point(event.getAxisValue(0, 0).toInt(), event.getAxisValue(1, 0).toInt())
        val secondFinger = Point(event.getAxisValue(0, 1).toInt(), event.getAxisValue(1, 1).toInt())


        val delta = Vector(lastPoint, point)

        Log.i("Scrolling", "First: $firstFinger")
        Log.i("Scrolling", "Second: $secondFinger")
        Log.i("Scrolling", "Point: $point")
        Log.i("Scrolling", "Event: ${event.getAxisValue(0, 0)} ${event.getAxisValue(1, 0)}")
        Log.i("Scrolling", delta.toString())
        Log.i("Scrolling", Vector(lastScrollCenter, PointInteractor.centerOfMass(arrayOf(firstFinger, secondFinger))).toString())


        lastScrollCenter = PointInteractor.centerOfMass(arrayOf(firstFinger, secondFinger))
        VectorInteractor().multiplyByFloat(delta, SCROLLING_SPEED)
        drawGraphView.graph.moveByVector(delta)
        drawGraphView.invalidate()
    }

    private fun scrollingUp(event: MotionEvent?) {
        if (event == null) return
        inputMode = InputMode.DEFAULT
        lastPoint = Point(event)
        stroke = Stroke()
    }

    private fun movementStart(event: MotionEvent?) {
        val point = event?.let { Point(it) }
        Log.i("movement", "Started moving")
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

    private fun movementMove(event: MotionEvent?) {
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

    private fun movementUp(event: MotionEvent?) {
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
        movementType = MovementType.CHILL
        drawGraphView.invalidate()
    }

    fun deleteEditingFigure() {
        if (inputMode != InputMode.EDITING)return
        closeEditing(lastEditingFigure)
        drawGraphView.graph.delete(lastEditingFigure)
        drawGraphView.invalidate()
    }

    companion object {
        private const val MICROSECOND = 1000000f // nanosecond / microsecond = milisecond
        private const val SCROLLING_THRESHOLD = 200
        private const val SCROLLING_SPEED = 1f
    }
}