package com.example.ochev.viewclasses.buttonshandler

import android.view.View
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.viewclasses.DrawGraphView

class ButtonsHandler (
    val buttonsContainer: ButtonsContainer,
    private val graphView: DrawGraphView
) {

    init{
        buttonsContainer.clearButton.setOnClickListener {
            closeEditing()
            graphView.graphEditor.clear()
            graphView.invalidate()
        }

        buttonsContainer.undoButton.setOnClickListener {
            closeEditing()
            graphView.graphEditor.revertChange()
            graphView.invalidate()
        }

        buttonsContainer.forwardButton.setOnClickListener {
            closeEditing()
            graphView.graphEditor.undoRevertChange()
            graphView.invalidate()
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
            graphView.invalidate()
        }
    }

    fun closeEditing() {
        buttonsContainer.deleteButton.visibility = View.INVISIBLE
    }

    fun disableAll() {
        buttonsContainer.forwardButton.isClickable = false
        buttonsContainer.undoButton.isClickable = false
        buttonsContainer.clearButton.isClickable = false
        buttonsContainer.deleteButton.isClickable = false
    }

    fun activateAll() {
        buttonsContainer.forwardButton.isClickable = true
        buttonsContainer.undoButton.isClickable = true
        buttonsContainer.clearButton.isClickable = true
        buttonsContainer.deleteButton.isClickable = true
    }

}