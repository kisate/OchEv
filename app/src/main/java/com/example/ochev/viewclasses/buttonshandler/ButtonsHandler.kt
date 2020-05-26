package com.example.ochev.viewclasses.buttonshandler

import android.view.View
import com.example.ochev.baseclasses.editors.FigureEditor
import com.example.ochev.baseclasses.editors.edgeeditor.EdgeEditor
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.viewclasses.drawers.GraphDrawer

class ButtonsHandler (
    private val buttonsContainer: ButtonsContainer,
    private val graphDrawer: GraphDrawer
) {

    init{
        buttonsContainer.clearButton.setOnClickListener {
            hideDeleteButton()
            hideCopyButton()
            graphDrawer.graphEditor.clear()
            graphDrawer.invalidate()
        }

        buttonsContainer.undoButton.setOnClickListener {
            hideDeleteButton()
            hideCopyButton()
            graphDrawer.graphEditor.revertChange()
            graphDrawer.invalidate()
        }

        buttonsContainer.forwardButton.setOnClickListener {
            hideDeleteButton()
            hideCopyButton()
            graphDrawer.graphEditor.undoRevertChange()
            graphDrawer.invalidate()
        }
        buttonsContainer.saveButton.setOnClickListener {
            //TODO()
        }
    }

    fun showDeleteButton() {
        buttonsContainer.deleteButton.visibility = View.VISIBLE
    }
    fun showCopyButton() {
        buttonsContainer.copyButton.visibility = View.VISIBLE
    }

    fun hideDeleteButton() {
        buttonsContainer.deleteButton.visibility = View.INVISIBLE
    }

    fun hideCopyButton() {
        buttonsContainer.copyButton.visibility = View.INVISIBLE
    }

    fun enterEditing(vertexFigureEditor: VertexFigureEditor) {
        initDeleteButton(vertexFigureEditor)
        initCopyButton(vertexFigureEditor)
    }

    fun enterEditing(edgeEditor: EdgeEditor) {
        initDeleteButton(edgeEditor)
    }

    private fun initDeleteButton(figureEditor: FigureEditor) {
        showDeleteButton()
        buttonsContainer.deleteButton.setOnClickListener {
            figureEditor.graphEditor.deleteFigure(figureEditor.currentFigureState)
            hideDeleteButton()
            graphDrawer.invalidate()
        }
    }

    private fun initCopyButton(figureEditor: VertexFigureEditor) {
        showCopyButton()
        buttonsContainer.copyButton.setOnClickListener {
            figureEditor.createCopy()
            graphDrawer.invalidate()
        }
    }

    fun disableAll() {
        buttonsContainer.forwardButton.isClickable = false
        buttonsContainer.undoButton.isClickable = false
        buttonsContainer.clearButton.isClickable = false
        buttonsContainer.deleteButton.isClickable = false
        buttonsContainer.saveButton.isClickable = false
        buttonsContainer.copyButton.isClickable = false
    }

    fun enableAll() {
        buttonsContainer.forwardButton.isClickable = true
        buttonsContainer.undoButton.isClickable = true
        buttonsContainer.clearButton.isClickable = true
        buttonsContainer.deleteButton.isClickable = true
        buttonsContainer.saveButton.isClickable = true
        buttonsContainer.copyButton.isClickable = true
    }

}