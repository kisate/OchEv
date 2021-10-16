package com.example.ochev.baseclasses.editors.boardeditor

import android.graphics.Bitmap
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.Vector
import com.example.ochev.callbacks.BoardChangesListener
import com.example.ochev.callbacks.UserModeChangesListener

interface BoardViewer {
    fun createFigureByStrokes(bitmap: Bitmap, strokes: MutableList<Stroke>?): Boolean

    fun selectFigureByPoint(point: Point): BoardManipulator?

    fun clearBoard()

    fun saveToGallery()

    fun undoChange()

    fun redoChange()

    fun scaleBoard(centre: Point, scaleValue: Float)

    fun moveBoard(vector: Vector)

    fun addUserModeChangesListener(userModeChangesListener: UserModeChangesListener)

    fun removeUserModeChangesListener(toDelete: UserModeChangesListener)

    fun addUserModeChangesListenerAndNotify(userModeChangesListener: UserModeChangesListener)

    fun addBoardChangesListener(boardChangeListener: BoardChangesListener)

    fun removeBoardChangesListener(toDelete: BoardChangesListener)

    fun addBoardChangesListenerAndNotify(boardChangeListener: BoardChangesListener)
}