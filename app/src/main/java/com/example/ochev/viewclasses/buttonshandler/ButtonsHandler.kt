package com.example.ochev.viewclasses.buttonshandler

import android.util.Log
import android.view.View
import com.example.ochev.baseclasses.editors.FigureEditor
import com.example.ochev.baseclasses.editors.edgeeditor.EdgeEditor
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.viewclasses.drawers.GraphDrawer
import com.example.ochev.viewclasses.eventhandlers.GestureHandler

class ButtonsHandler (
    private val buttonsContainer: ButtonsContainer,
    private val graphDrawer: GraphDrawer
) {

    lateinit var gestureHandler: GestureHandler

    init{
        buttonsContainer.clearButton.setOnClickListener {
            gestureHandler.exitAll()
            hideDeleteButton()
            hideCopyButton()
            graphDrawer.graphEditor.clear()
            graphDrawer.invalidate()
        }

        buttonsContainer.undoButton.setOnClickListener {
            gestureHandler.exitAll()
            hideDeleteButton()
            hideCopyButton()
            graphDrawer.graphEditor.revertChange()
            graphDrawer.invalidate()
        }

        buttonsContainer.forwardButton.setOnClickListener {
            gestureHandler.exitAll()
            hideDeleteButton()
            hideCopyButton()
            graphDrawer.graphEditor.undoRevertChange()
            graphDrawer.invalidate()
        }
        buttonsContainer.saveButton.setOnClickListener {
            graphDrawer.saveToPng()
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
            gestureHandler.exitAll()
            figureEditor.graphEditor.deleteFigure(figureEditor.figureId)
            hideDeleteButton()
            hideCopyButton()
            graphDrawer.invalidate()
        }
    }

    private fun initCopyButton(figureEditor: VertexFigureEditor) {
        showCopyButton()
        buttonsContainer.copyButton.setOnClickListener {
            Log.i("COPYDBG1", graphDrawer.graphEditor.graph.figures.vertices.toString())
            figureEditor.createCopy(graphDrawer.getCentre())
            Log.i("COPYDBG1", graphDrawer.graphEditor.graph.figures.vertices.toString())
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