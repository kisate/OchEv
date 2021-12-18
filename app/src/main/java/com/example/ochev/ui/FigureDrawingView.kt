package com.example.ochev.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import android.widget.LinearLayout
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
import com.example.ochev.baseclasses.editors.edgefigures.Edge

class FigureDrawingView(
    context: Context,
    attributeSet: AttributeSet,
) : FrameLayout(context, attributeSet) {
    private val mapper: HashMap<Int, TextView> = HashMap()
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

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        Log.e(TAG, "on draw with ${figures.size} figures")

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
                    return
                }
                val lp = LinearLayout.LayoutParams(
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
                mapper[figureNode.id] = view
            }
        }

        for (segment in suggests) {
            val path = Path()
            path.moveTo(segment.A.x, segment.A.y)
            path.lineTo(segment.B.x, segment.B.y)
            canvas?.drawPath(path, paintSuggests)
        }
    }

    fun setProvider(provider: Provider<Int?>) {
        idProvider = provider
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
        this.addView(view)
        pull.add(view)
    }

    companion object {
        private val TAG = "FigureDrawingView"
    }

}