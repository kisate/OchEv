package com.example.ochev.baseclasses.editors.vertexeditor

import com.example.ochev.baseclasses.dataclasses.InformationForVertexEditor
import com.example.ochev.baseclasses.vertexfigures.VertexFigure

class VertexFigureEditor(
    information: InformationForVertexEditor
) {
    var figureUnderControl = information.figure
    val graphEditor = information.graphEditor

    fun changeFigure(newFigure: VertexFigure) {
        graphEditor.replaceVertex(figureUnderControl, newFigure)
        figureUnderControl = newFigure
    }

    val mover = VertexFigureMover(this)
    val shaper = VertexFigureShaper(this)
}