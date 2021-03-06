package com.example.ochev.baseclasses.editors.vertexeditor

import android.util.Log
import com.example.ochev.baseclasses.dataclasses.InformationForVertexEditor
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Vector
import com.example.ochev.baseclasses.dataclasses.nodes.FigureNode
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure
import com.example.ochev.baseclasses.editors.FigureEditor

class VertexFigureEditor(
    information: InformationForVertexEditor
) : FigureEditor {
    override val figureId: Int = information.id
    override val currentFigureState: VertexFigure
        get() = graphEditor.graph.getVertexFigureNodeByIdOrNull(figureId)!!.figure
    override val graphEditor = information.graphEditor
    override val figureNode: FigureNode
        get() = graphEditor.graph.getVertexFigureNodeByIdOrNull(figureId)!!

    fun changeFigure(newFigure: VertexFigure) {
        graphEditor.replaceVertex(figureId, newFigure)
    }

    fun createCopy(canvasCenter: Point = Point()) {
        graphEditor.addFigure(
            currentFigureState.clone()
                .movedByVector(Vector(currentFigureState.center, canvasCenter))
        )
        Log.i("CopyDBG", graphEditor.allVertexes.toString())
    }

    val mover = VertexFigureMover(this)
    val shaper = VertexFigureShaper(this)
}