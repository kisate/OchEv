package com.example.ochev.viewclasses.graphdrawers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.dataclasses.InformationForNormalizer
import com.example.ochev.baseclasses.edgefigures.Edge
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor
import com.example.ochev.baseclasses.vertexfigures.Circle
import com.example.ochev.baseclasses.vertexfigures.Rectangle
import com.example.ochev.viewclasses.DrawGraphView
import com.example.ochev.viewclasses.DrawingInformation
import com.example.ochev.viewclasses.DrawingMode

class GraphDrawer(
    val graphView: DrawGraphView
) {
    var scale = 1f
    fun clear(){
        graphView.graphEditor.clear()
        graphView.invalidate()
    }

    fun modifyByStrokes(information: InformationForNormalizer) {
        graphView.graphEditor.modifyByStrokes(information)
    }

}
