package com.example.ochev.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.children
import com.example.ochev.Utils.Provider
import com.example.ochev.baseclasses.dataclasses.LineSegment
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.nodes.FigureNode
import com.example.ochev.baseclasses.dataclasses.nodes.VertexFigureNode
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Circle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rectangle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rhombus
import com.example.ochev.baseclasses.editors.boardeditor.BoardManipulator
import com.example.ochev.baseclasses.editors.edgefigures.Edge
import android.view.Gravity




class FigureDrawingView(
    context: Context,
    attributeSet: AttributeSet,
) : FrameLayout(context, attributeSet) {
    private var mapper: HashMap<Int, TextView> = HashMap()
    private val pull: ArrayList<TextView> = ArrayList()

    init {
        for (child in children) {
            pull.add(child as TextView)
        }
        pull.forEach {
            it.text = ""
        }
        while (pull.size < 5) {
            inflateToPull()
        }
        setWillNotDraw(false)
    }

    var figures: List<FigureNode> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    var suggests: List<LineSegment> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    var paintStroke: Paint = Paint()
    var paintFill: Paint = Paint()
    var paintSuggests: Paint = Paint()

    private var idProvider: Provider<Int?>? = null
    private var manipulator: Provider<BoardManipulator?>? = null

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        Log.e(TAG, "on draw with ${figures.size} figures")
        val binded = HashMap<Int, TextView>()
        figures.forEach { figureNode ->
            if (figureNode is VertexFigureNode) {
                mapper[figureNode.id]?.let { binded[figureNode.id] = it }
            }
        }
        for (child in children) {
            if (child !in binded.values) {
                child as TextView
                child.text = ""
                child.visibility = GONE
                if (child !in pull) {
                    pull.add(child)
                }
            }
        }
        mapper = binded

        for (figureNode in figures) {
            val currentWidth = paintStroke.strokeWidth
            if (figureNode.id == idProvider?.get()) {
                paintStroke.strokeWidth = currentWidth * 3
            }
            when (val figure = figureNode.figure) {
                is Rectangle -> drawRectangle(canvas, figure)
                is Rhombus -> drawRhombus(canvas, figure)
                is Circle -> drawCircle(canvas, figure)
                is Edge -> drawEdge(canvas, figure)
            }
            paintStroke.strokeWidth = currentWidth
            if (figureNode is VertexFigureNode) {
                var view = mapper[figureNode.id]
                if (view == null) {
                    if (pull.isEmpty()) {
                        inflateToPull()
                    }
                    view = pull.removeLast()
                }
                if (!figureNode.textInfo.changed) {
                    continue
                }
                val lp = LayoutParams(
                    (figureNode.textInfo.rightUpCorner.x - figureNode.textInfo.leftDownCorner.x).toInt(),
                    (figureNode.textInfo.leftDownCorner.y - figureNode.textInfo.rightUpCorner.y).toInt()
                )
                lp.setMargins(
                    figureNode.textInfo.leftDownCorner.x.toInt(),
                    figureNode.textInfo.rightUpCorner.y.toInt(),
                    0,
                    0
                )
                view.layoutParams = lp
                view.text = figureNode.textInfo.text
                view.visibility = VISIBLE
                view.textSize = figureNode.textInfo.fontSize.toFloat()
                mapper[figureNode.id] = view
            }
        }

        for (segment in suggests) {
            val path = Path()
            path.moveTo(segment.A.x, segment.A.y)
            path.lineTo(segment.B.x, segment.B.y)
            canvas?.drawPath(path, paintSuggests)
        }

        manipulator?.get()?.figureDrawn()
    }

    fun setProvider(provider: Provider<Int?>) {
        idProvider = provider
    }

    fun setManipulatorProvider(provider: Provider<BoardManipulator?>) {
        this.manipulator = provider
    }

    private fun drawEdge(canvas: Canvas?, figure: Edge) {
        val points = listOf(
            figure.from.figure.center,
            figure.to.figure.center
        )
        drawPoints(points, canvas)
    }

    private fun drawRhombus(canvas: Canvas?, figure: Rhombus) {
        val points =
            listOf(
                figure.leftCorner,
                figure.upCorner,
                figure.rightCorner,
                figure.downCorner,
            )
        drawPoints(points, canvas)
    }

    private fun drawRectangle(canvas: Canvas?, figure: Rectangle) {
        val points =
            listOf(
                figure.leftDownCorner,
                figure.rightDownCorner,
                figure.rightUpCorner,
                figure.leftUpCorner,
            )
        drawPoints(points, canvas)
    }

    private fun drawCircle(canvas: Canvas?, figure: Circle) {
        canvas?.drawCircle(figure.center.x, figure.center.y, figure.radius, paintStroke)
        canvas?.drawCircle(figure.center.x, figure.center.y, figure.radius, paintFill)
    }

    private fun drawPoints(points: List<Point>, canvas: Canvas?) {
        val path = Path()
        path.moveTo(points[0].x, points[0].y)
        for (i in 1 until points.size) {
            val point = points[i]
            path.lineTo(point.x, point.y)
        }
        path.close()
        canvas?.drawPath(path, paintStroke)
        canvas?.drawPath(path, paintFill)
    }

    private fun inflateToPull() {
        val view = TextView(context)
        view.isClickable = false
        view.isFocusable = false
        view.setBackgroundColor(Color.TRANSPARENT)
        pull.add(view)
        view.typeface = Typeface.create("open_sans_bold", Typeface.BOLD)
        view.setTextColor(Color.BLACK)
        view.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
        addView(view)
    }

    companion object {
        private val TAG = "FigureDrawingView"
    }

}