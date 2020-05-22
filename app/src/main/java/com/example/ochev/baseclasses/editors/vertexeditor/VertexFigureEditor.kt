package com.example.ochev.baseclasses.editors.vertexeditor

import com.example.ochev.baseclasses.dataclasses.InformationForVertexEditor
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure
import com.example.ochev.baseclasses.editors.FigureEditor

class VertexFigureEditor(
    information: InformationForVertexEditor
) : FigureEditor {
    override val figureId: Int = information.id
    override val currentFigureState: VertexFigure
        get() = graphEditor.getVertexFigureNodeByIdOrNull(figureId)!!.figure
    override val graphEditor = information.graphEditor

    fun changeFigure(newFigure: VertexFigure) {
        graphEditor.replaceVertex(
            currentFigureState,
            newFigure
        )
    }

    val mover = VertexFigureMover(this)
    val shaper = VertexFigureShaper(this)
}