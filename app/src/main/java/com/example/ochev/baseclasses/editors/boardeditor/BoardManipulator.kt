package com.example.ochev.baseclasses.editors.boardeditor

import com.example.ochev.baseclasses.dataclasses.Point

interface BoardManipulator {
    fun putPoint(pt: Point): BoardManipulator?

    fun startEditing(pt: Point)

    fun cancelEditing(pt: Point)

    fun deleteSelected()

    fun copySelected()

    fun getId(): Int

    fun putText(text: String)

    fun figureDrawn()

    fun setFontSize(fontSize: Int)

    fun getCurrentText(): String

    fun startFontSizeChanging()

    fun finishFontSizeChanging()

    fun startEditingText()
}