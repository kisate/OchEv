package com.example.ochev.viewclasses.buttonshandler

import android.view.View
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.viewclasses.graphdrawers.GraphDrawer

class ButtonsHandler (
    val buttonsContainer: ButtonsContainer,
    private val graphDrawer: GraphDrawer
) {

    init{
        buttonsContainer.clearButton.setOnClickListener {
            closeEditing()
            graphDrawer.clear()
            graphDrawer.graphView.invalidate()
        }

    }

    fun showDeleteButton() {
        buttonsContainer.deleteButton.visibility = View.VISIBLE
    }

    fun enterEditing(figureEditor: VertexFigureEditor) {
        showDeleteButton()
        buttonsContainer.deleteButton.setOnClickListener {
            figureEditor.graphEditor.deleteFigure(figureEditor.currentFigureState)
            closeEditing()
            graphDrawer.graphView.invalidate()
        }
    }

    fun closeEditing() {
        buttonsContainer.deleteButton.visibility = View.INVISIBLE
    }


}