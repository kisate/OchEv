package com.example.ochev.viewclasses.drawers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.core.graphics.withTranslation
import com.example.ochev.MainActivity
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
        val restrictions = graphEditor.graph.getGraphRestrictions()
        if (restrictions != null)
        {
            val maxX = restrictions[0]
            val maxY = restrictions[1]
            val minX = restrictions[2]
            val minY = restrictions[3]

            Log.d("Restrictions", restrictions.joinToString(" "))

            val width = maxX - minX + PNG_BORDERS*2
            val height = maxY - minY + PNG_BORDERS*2

            val bitmap = Bitmap.createBitmap(
                width.toInt(),
                height.toInt(),
                Bitmap.Config.ARGB_8888
            )

            val canvas = Canvas(bitmap)

            canvas.withTranslation (-minX + PNG_BORDERS, -minY + PNG_BORDERS) { drawGraphOnCanvas(graphEditor, this) }

            val scale = max(1f, max(width/ MAX_WIDTH, height/ MAX_HEIGHT))

            Utils.saveBitmapToGallery(Bitmap.createScaledBitmap(bitmap, (width/scale).toInt(), (height/scale).toInt(), true), graphView.context as MainActivity, "graph.png")
        }
    }

    companion object {
        private const val PNG_BORDERS = 50f
        private const val MAX_WIDTH = 2000f
        private const val MAX_HEIGHT = 2000f
    }
}
