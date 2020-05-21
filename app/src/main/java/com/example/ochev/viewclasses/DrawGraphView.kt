package com.example.ochev.viewclasses

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor
import com.example.ochev.viewclasses.graphdrawers.FiguresDrawer

class DrawGraphView(
    context: Context?,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    val graphEditor = GraphEditor()

    val figuresDrawer =
        FiguresDrawer()

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawColor(Color.LTGRAY)
        for (figure in graphEditor.graph.figures.figuresSortedByHeights) {
            Log.i("ClassifyDbg", figure.toString())
            figuresDrawer.draw(figure.figure, figure.drawingInformation, canvas)
        }
    }
}