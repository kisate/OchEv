package com.example.ochev.viewclasses

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.ochev.baseclasses.dataclasses.nodes.FigureNode
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor
import com.example.ochev.viewclasses.drawers.FiguresDrawer
import com.example.ochev.viewclasses.drawers.drawinginformations.DrawingMode

class DrawGraphView(
    context: Context?,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val figuresDrawer =
        FiguresDrawer()

    private var currentGraph = GraphEditor()

    fun invalidate(graphEditor: GraphEditor) {
        currentGraph = graphEditor
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        drawGraphOnCanvas(currentGraph, canvas)
    }

    fun drawGraphOnCanvas(graphEditor: GraphEditor, canvas: Canvas?) {
        canvas?.drawColor(Color.LTGRAY)
        val repeating = ArrayList<FigureNode>()
        for (figure in graphEditor.graph.figures.figuresSortedByHeights) {
            Log.i("ClassifyDbg", figure.toString())
            figuresDrawer.draw(figure.figure, figure.drawingInformation, canvas)
            if (figure.drawingInformation.drawingMode == DrawingMode.EDIT) {
                repeating.add(figure)
            }
        }
        for (figure in repeating) {
            Log.i("ClassifyDbg", figure.toString())
            figuresDrawer.draw(figure.figure, figure.drawingInformation, canvas)
        }
    }
}