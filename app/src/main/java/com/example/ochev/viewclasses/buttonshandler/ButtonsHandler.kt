package com.example.ochev.viewclasses.buttonshandler

import android.view.View
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.viewclasses.DrawGraphView
import com.example.ochev.viewclasses.DrawingMode
import com.example.ochev.viewclasses.graphdrawers.GraphDrawer

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
            graphView.graphEditor.history.revert()
            graphView.graphEditor.graph.figures.edges.forEach{it.drawingInformation.drawingMode = DrawingMode.DEFAULT}
            graphView.graphEditor.graph.figures.vertices.forEach{it.drawingInformation.drawingMode = DrawingMode.DEFAULT}
            graphView.invalidate()
        }

        buttonsContainer.forwardButton.setOnClickListener {
            graphView.graphEditor.history.undoRevert()
            graphView.graphEditor.graph.figures.edges.forEach{it.drawingInformation.drawingMode = DrawingMode.DEFAULT}
            graphView.graphEditor.graph.figures.vertices.forEach{it.drawingInformation.drawingMode = DrawingMode.DEFAULT}
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


}