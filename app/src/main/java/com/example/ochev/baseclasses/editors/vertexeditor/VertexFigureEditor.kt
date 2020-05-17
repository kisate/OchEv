package com.example.ochev.baseclasses.editors.vertexeditor

import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.InformationForVertexEditor

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
    val rescaler = VertexFigureRescaler(this)
}