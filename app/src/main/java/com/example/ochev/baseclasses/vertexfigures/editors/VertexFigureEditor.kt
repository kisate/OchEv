package com.example.ochev.baseclasses.vertexfigures.editors

import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.InformationForVertexEditor

class VertexFigureEditor(
    information: InformationForVertexEditor
) {
    val figure: VertexFigure = information.figure
    val mover = VertexFigureMover(information)
    val rescaler = VertexFigureRescaler(information)
}