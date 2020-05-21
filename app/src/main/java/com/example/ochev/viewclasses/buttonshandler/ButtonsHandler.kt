package com.example.ochev.viewclasses.buttonshandler

import android.view.View
import android.widget.Button
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.viewclasses.GraphDrawer

class ButtonsHandler (
    clearButton: Button,
    deleteButton: Button,
    private val graphDrawer: GraphDrawer
) {
    val buttonsContainer = ButtonsContainer(clearButton, deleteButton)

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