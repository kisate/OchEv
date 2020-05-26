package com.example.ochev.viewclasses.drawers

import android.graphics.Canvas
import com.example.ochev.baseclasses.dataclasses.InformationForNormalizer
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor
import com.example.ochev.viewclasses.DrawGraphView

class GraphDrawer(
    val graphEditor: GraphEditor = GraphEditor()
) {
    var scale = 1f

    lateinit var graphView: DrawGraphView

    fun modifyByStrokes(information: InformationForNormalizer) : Boolean {
        return graphEditor.modifyByStrokes(information)
    }

    fun invalidate() {
        graphView.invalidate(graphEditor)
    }

    fun drawGraphOnCanvas(graphEditor: GraphEditor, canvas: Canvas?) {
        graphView.drawGraphOnCanvas(graphEditor, canvas)
    }

}
