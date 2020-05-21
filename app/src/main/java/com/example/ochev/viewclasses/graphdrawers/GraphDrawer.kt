package com.example.ochev.viewclasses.graphdrawers

import com.example.ochev.baseclasses.dataclasses.InformationForNormalizer
import com.example.ochev.viewclasses.DrawGraphView

class GraphDrawer(
    val graphView: DrawGraphView
) {
    var scale = 1f

    fun modifyByStrokes(information: InformationForNormalizer) {
        graphView.graphEditor.modifyByStrokes(information)
    }

}
