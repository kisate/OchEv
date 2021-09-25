package com.example.ochev.viewclasses

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor

@SuppressLint("AppCompatCustomView")
class SmartEditText(context: Context?, attrs: AttributeSet?) :
    EditText(context, attrs) {
    private var vertexFigureEditor: VertexFigureEditor? = null
    private var drawGraphView: DrawGraphView? = null

    fun setVertexEditor(newVertexFigureEditor: VertexFigureEditor, drawGraphView: DrawGraphView) {
        vertexFigureEditor = newVertexFigureEditor
        this.drawGraphView = drawGraphView
        setText(
            newVertexFigureEditor.graphEditor.graph.getFigureNodeByIdOrNull(
                newVertexFigureEditor.figureId
            )!!.drawingInformation.text
        )

    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (vertexFigureEditor != null) {
            vertexFigureEditor!!.graphEditor.graph.getFigureNodeByIdOrNull(vertexFigureEditor!!.figureId)!!.drawingInformation.text =
                text.toString().trim()
            drawGraphView?.invalidate()
        }
    }

}