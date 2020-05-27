package com.example.ochev.viewclasses.drawers

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.graphics.withTranslation
import com.example.ochev.baseclasses.dataclasses.InformationForNormalizer
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor
import com.example.ochev.ml.Utils
import com.example.ochev.viewclasses.DrawGraphView
import java.lang.Float.max

class GraphDrawer(
    val graphEditor: GraphEditor = GraphEditor()
) {
    var scale = 1f

    fun getHeight(): Int {
        return graphView.height
    }

    fun getWidth(): Int {
        return graphView.width
    }

    fun getCentre() : Point {
        return Point(getWidth()/2f, getHeight()/2f)
    }

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

    fun saveToPng() {
//        val restrictions = graphEditor.getGraphRestrictions()
//        if (restrictions != null)
//        {
//            val maxX = restrictions[0]
//            val maxY = restrictions[1]
//            val minX = restrictions[2]
//            val minY = restrictions[3]
//
//            val width = maxX - minX
//            val height = maxY - minY
//
//            val bitmap = Bitmap.createBitmap(
//                width.toInt(),
//                height.toInt(),
//                Bitmap.Config.ARGB_8888
//            )
//
//            val canvas = Canvas(bitmap)
//
//            graphView.layout(minX.toInt(), maxY.toInt(), maxX.toInt(), minY.toInt())
//
//            canvas.withTranslation (max(0f, -minX), max(0f, -minY)) { drawGraphOnCanvas(graphEditor, this) }
//
//            Utils.saveBitmap(bitmap, graphView.context, "graph.png")
//        }
    }
}
