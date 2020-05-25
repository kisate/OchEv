package com.example.ochev.viewclasses.buttonshandler

import android.view.View
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.viewclasses.DrawGraphView
import com.example.ochev.viewclasses.graphdrawers.GraphDrawer

class ButtonsHandler (
    val buttonsContainer: ButtonsContainer,
    private val graphDrawer: GraphDrawer
) {

    init{
        buttonsContainer.clearButton.setOnClickListener {
            closeEditing()
            graphDrawer.graphEditor.clear()
            graphDrawer.invalidate()
        }

        buttonsContainer.undoButton.setOnClickListener {
            closeEditing()
            graphDrawer.graphEditor.revertChange()
            graphDrawer.invalidate()
        }

        buttonsContainer.forwardButton.setOnClickListener {
            closeEditing()
            graphDrawer.graphEditor.undoRevertChange()
            graphDrawer.invalidate()
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
            graphDrawer.invalidate()
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