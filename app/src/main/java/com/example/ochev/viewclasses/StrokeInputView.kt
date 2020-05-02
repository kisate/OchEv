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
    private val funMap = HashMap<InputMode, HashMap<Int, (InputHandler, Point) -> Unit>> ()

    var inputMode = InputMode.DRAWING

    fun clear() {
        inputHandler.clear()
    }

    private fun addToFunMap(mode : InputMode, eventAction : Int, function : (InputHandler, Point) -> Unit)
    {
        funMap[mode]!![eventAction] = function
    }

    init {
        addToFunMap(InputMode.DRAWING, MotionEvent.ACTION_DOWN) {
                inputHandler, point -> inputHandler.touchStart(point)
        }
        addToFunMap(InputMode.DRAWING, MotionEvent.ACTION_MOVE) {
                inputHandler, point -> inputHandler.touchMove(point)
        }
        addToFunMap(InputMode.DRAWING, MotionEvent.ACTION_UP) {
                inputHandler, point -> inputHandler.touchUp(point)
        }

        addToFunMap(InputMode.EDITING, MotionEvent.ACTION_DOWN) {
                inputHandler, point -> inputHandler.movementStart(point)
        }
        addToFunMap(InputMode.EDITING, MotionEvent.ACTION_MOVE) {
                inputHandler, point -> inputHandler.movementMove(point)
        }
        addToFunMap(InputMode.EDITING, MotionEvent.ACTION_UP) {
                inputHandler, point -> inputHandler.movementUp(point)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var point: Point? = null
        throttle.attempt(Runnable { point = Point(event.x.toInt(), event.y.toInt()) })

        Log.i("Touch", event.pointerCount.toString())

        point?.let { (funMap[inputMode]!![event.action]!!)(inputHandler, it) }

        return true
    }
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

    private var lastTime = 0L
    private val MICROSECOND = 1000000f // nanosecond / microsecond = milisecond
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


    fun touchMove(point: Point?) {
        if (point == null) return
        if (PointInteractor().distance(point, lastPoint) <= ACCURACY) return
        stroke.addPoint(point)
        drawStrokeInteractor.set(drawStrokeView, stroke)
        lastPoint = point
    }

    fun touchUp(point: Point?) {
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

    fun touchStart(point: Point?) {
        if (point == null) return
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

    fun movementStart(point: Point?) {
        if (point != null) {
            if (vertexFigureEditor != null) {
                if (!vertexFigureEditor!!.mover.tryToStartMove(point)) {
                    //closeEditing(lastEditingFigure)
                    vertexFigureEditor = null
                }
            } else {
                vertexFigureEditor = null
            }
        }
    }

    fun movementMove(point: Point?) {
        if (point != null && vertexFigureEditor != null) {
            vertexFigureEditor!!.mover.newPoint(point)
            drawGraphView.invalidate()
        }
    }

    fun movementUp(point: Point?) {
        if (vertexFigureEditor == null){
            closeEditing(lastEditingFigure)
        }
        drawGraphView.invalidate()
    }
}