package com.example.ochev.viewclasses

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.EditText
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor

@SuppressLint("AppCompatCustomView")
class SmartEditText(context: Context?, attrs: AttributeSet?) :
    EditText(context, attrs) {
    private var vertexFigureEditor: VertexFigureEditor? = null

    fun setVertexEditor(newVertexFigureEditor: VertexFigureEditor) {
        vertexFigureEditor = newVertexFigureEditor
        setText(newVertexFigureEditor.graphEditor.getFigureNodeByIdOrNull(newVertexFigureEditor.figureId)!!.drawingInformation.text)

    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (vertexFigureEditor != null)
        {
            Log.d("text", text.toString())
            vertexFigureEditor!!.graphEditor.getFigureNodeByIdOrNull(vertexFigureEditor!!.figureId)!!.drawingInformation.text = text.toString()
        }
    }
}