package com.example.ochev.baseclasses.editors.boardeditor

import android.graphics.Bitmap
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.callbacks.UserModeChangesListener
import com.example.ochev.ml.Classifier

interface BoardViewer {
    fun createFigureByStrokes(bitmap: Bitmap, strokes: MutableList<Stroke>?): Boolean

    fun selectFigureByPoint(point: Point): BoardManipulator

    fun clearBoard()

    fun saveToGallery()

    fun undoChange()

    fun redoChange()

    fun scaleBoard(centre: Point, scaleValue: Float)

    fun addListener(userModeChangesListener: UserModeChangesListener)

    fun addListenerAndNotify(userModeChangesListener: UserModeChangesListener)
}