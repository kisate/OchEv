package com.example.ochev.baseclasses.dataclasses

import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor
import com.example.ochev.baseclasses.vertexfigures.VertexFigure

data class InformationForVertexEditor(
    val figure: VertexFigure,
    val graphEditor: GraphEditor
)