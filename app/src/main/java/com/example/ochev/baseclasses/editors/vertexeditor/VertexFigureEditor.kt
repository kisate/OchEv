package com.example.ochev.baseclasses.editors.vertexeditor

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.InformationForVertexEditor

class VertexFigureEditor(
    information: InformationForVertexEditor
) {
    var figureUnderControl = information.figure
    val graphUnderControl = information.graph

    @RequiresApi(Build.VERSION_CODES.N)
    fun changeFigure(newFigure: VertexFigure) {
        graphUnderControl.figures.replaceVertex(figureUnderControl, newFigure)
        figureUnderControl = newFigure
    }

    val mover = VertexFigureMover(this)
    val rescaler = VertexFigureRescaler(this)
}