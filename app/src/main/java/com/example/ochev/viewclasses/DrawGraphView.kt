package com.example.ochev.viewclasses

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.dataclasses.Graph
import com.example.ochev.baseclasses.edgefigures.Line
import com.example.ochev.baseclasses.vertexfigures.Circle

class DrawGraphView (
    context: Context?,
    attrs: AttributeSet? = null

) : View(context, attrs) {
    val graph = Graph()
}

class DrawGraphInteractor {
    fun add(drawGraphView: DrawGraphView, figure: Figure) {
        when (figure) {
            is Circle -> addCircle(drawGraphView, figure)
        }
    }

    private fun addCircle(drawGraphView: DrawGraphView, figure: Circle) {

    }

}