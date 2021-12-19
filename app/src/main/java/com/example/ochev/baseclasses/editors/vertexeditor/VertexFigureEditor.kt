package com.example.ochev.baseclasses.editors.vertexeditor

import android.util.Log
import com.example.ochev.baseclasses.dataclasses.InformationForVertexEditor
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Vector
import com.example.ochev.baseclasses.dataclasses.nodes.FigureNode
import com.example.ochev.baseclasses.dataclasses.nodes.VertexFigureNode
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure
import com.example.ochev.baseclasses.editors.FigureEditor

class VertexFigureEditor(
    information: InformationForVertexEditor
) : FigureEditor {
    override val figureId: Int = information.id
    override val currentFigureState: VertexFigure
        get() = graphEditor.graph.getVertexFigureNodeByIdOrNull(figureId)!!.figure
    override val graphEditor = information.graphEditor
    override val figureNode: VertexFigureNode
        get() = graphEditor.graph.getVertexFigureNodeByIdOrNull(figureId)!!

    fun changeFigure(newFigure: VertexFigure) {
        graphEditor.replaceVertex(
            figureId,
            figureNode.copy(figure = newFigure, textInfo = figureNode.textInfo.update(newFigure))
        )
    }

    fun createCopy(canvasCenter: Point = Point()) {
        val tmpFigure = currentFigureState.movedByVector(
            Vector(
                currentFigureState.center,
                canvasCenter
            )
        )
        graphEditor.addFigure(
            figureNode.copy(
                figure = tmpFigure,
                textInfo = figureNode.textInfo.update(tmpFigure)
            )
        )
        Log.i("CopyDBG", graphEditor.allVertexes.toString())
    }

    val mover = VertexFigureMover(this)
    val shaper = VertexFigureShaper(this)
}