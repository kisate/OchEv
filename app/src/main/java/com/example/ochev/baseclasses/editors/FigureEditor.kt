package com.example.ochev.baseclasses.editors

import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.nodes.FigureNode
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor

interface FigureEditor {
    val figureId: Int
    val graphEditor: GraphEditor
    val currentFigureState: Figure
    val figureNode: FigureNode
}