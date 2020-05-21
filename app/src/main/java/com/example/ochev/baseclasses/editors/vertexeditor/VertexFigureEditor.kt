package com.example.ochev.baseclasses.editors.vertexeditor

import com.example.ochev.baseclasses.dataclasses.InformationForVertexEditor
import com.example.ochev.baseclasses.vertexfigures.VertexFigure

class VertexFigureEditor(
    information: InformationForVertexEditor
) {
    val figureId: Int = information.id
    lateinit var currentFigureState: VertexFigure
    val graphEditor = information.graphEditor

    fun updateFigure() {
        currentFigureState = graphEditor.getVertexFigureNodeByIdOrNull(figureId)!!.figure
    }
    fun changeFigure(newFigure: VertexFigure) {
        graphEditor.replaceVertex(
            graphEditor.getVertexFigureNodeByIdOrNull(figureId)!!.figure,
            newFigure
        )
        currentFigureState = newFigure
    }

    val mover = VertexFigureMover(this)
    val shaper = VertexFigureShaper(this)
}