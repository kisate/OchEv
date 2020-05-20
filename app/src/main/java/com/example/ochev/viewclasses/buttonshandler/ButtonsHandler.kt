package com.example.ochev.viewclasses.buttonshandler

import android.view.View
import android.widget.Button
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.viewclasses.GraphDrawer
import com.example.ochev.viewclasses.StrokeDrawer
import com.example.ochev.viewclasses.StrokeInputView
import kotlinx.android.synthetic.main.activity_main.*

class ButtonsHandler (
    clearButton: Button,
    deleteButton: Button,
    strokeDrawer: StrokeDrawer,
    private val graphDrawer: GraphDrawer
) {
    val buttonsContainer = ButtonsContainer(clearButton, deleteButton)

    init{
        buttonsContainer.clearButton.setOnClickListener {
            strokeDrawer.clear()
            graphDrawer.graphView.invalidate()
        }
    }

    fun showDeleteButton() {
        buttonsContainer.deleteButton.visibility = View.VISIBLE
    }

    fun enterEditing(figureEditor: VertexFigureEditor) {
        showDeleteButton()
        buttonsContainer.deleteButton.setOnClickListener {
            figureEditor.graphEditor.deleteFigure(figureEditor.figureUnderControl)
            graphDrawer.graphView.invalidate()
        }
    }

    fun closeEditing() {
        buttonsContainer.deleteButton.visibility = View.INVISIBLE
    }


}