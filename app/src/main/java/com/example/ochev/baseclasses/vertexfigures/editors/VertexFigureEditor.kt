package com.example.ochev.baseclasses.vertexfigures.editors

import com.example.ochev.baseclasses.dataclasses.InformationForVertexEditor

class VertexFigureEditor(
    information: InformationForVertexEditor
) {
    val mover = VertexFigureMover(information)

}