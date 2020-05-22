package com.example.ochev.baseclasses.editors.edgeeditor

import com.example.ochev.baseclasses.dataclasses.Figure
import com.example.ochev.baseclasses.dataclasses.InformationForEdgeEditor
import com.example.ochev.baseclasses.editors.FigureEditor
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor

class EdgeEditor(
    val information: InformationForEdgeEditor
) : FigureEditor {
    override val figureId: Int = information.id
    override val currentFigureState: Figure
        get() = graphEditor.getEdgeNodeByIdOrNull(figureId)!!.figure
    override val graphEditor: GraphEditor = information.graphEditor
}