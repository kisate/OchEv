package com.example.ochev.baseclasses.editors.vertexeditor

import com.example.ochev.baseclasses.dataclasses.InformationForVertexEditor
import com.example.ochev.baseclasses.vertexfigures.VertexFigure

class VertexFigureEditor(
    information: InformationForVertexEditor
) {
    val figureId: Int = information.id
    val currentFigureState: VertexFigure
        get() = graphEditor.getVertexFigureNodeByIdOrNull(figureId)!!.figure
    val graphEditor = information.graphEditor

    fun changeFigure(newFigure: VertexFigure) {
        graphEditor.replaceVertex(
            currentFigureState,
            newFigure
        )
    }

    val mover = VertexFigureMover(this)
    val shaper = VertexFigureShaper(this)
}