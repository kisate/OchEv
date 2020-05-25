package com.example.ochev.viewclasses.graphdrawers

import com.example.ochev.baseclasses.dataclasses.InformationForNormalizer
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor
import com.example.ochev.viewclasses.DrawGraphView

class GraphDrawer(
    val graphView: DrawGraphView,
    val graphEditor: GraphEditor = GraphEditor()
) {
    var scale = 1f

    fun modifyByStrokes(information: InformationForNormalizer) {
        graphEditor.modifyByStrokes(information)
    }

    fun invalidate() {
        graphView.invalidate(graphEditor)
    }

}
