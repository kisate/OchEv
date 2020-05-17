package com.example.ochev.baseclasses.dataclasses

import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor

data class InformationForVertexEditor(
    val figure: VertexFigure,
    val graphEditor: GraphEditor
)