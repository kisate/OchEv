package com.example.ochev.baseclasses.editors.boardeditor

import android.graphics.Bitmap
import com.example.ochev.baseclasses.cacheparser.CacheParser
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.Vector
import com.example.ochev.callbacks.*

interface BoardViewer {
    fun onDestroy()

    fun getLastManipulator(): BoardManipulator?

    fun setGraphBitmap(bitmap: Bitmap)

    fun getGraphBitmap(): Bitmap?

    fun createFigureByStrokes(bitmap: Bitmap, strokes: MutableList<Stroke>?): Boolean

    fun selectFigureByPoint(point: Point): BoardManipulator?

    fun setLastEnterTimeMs(millis: Long)

    fun getLastEnterTimeMs(): Long

    fun setWindowParams(height: Int, width: Int)

    fun clearBoard()

    fun undoChange()

    fun redoChange()

    fun saveInCache(cacheParser: CacheParser)

    fun scaleBoard(centre: Point, scaleValue: Float)

    fun moveBoard(vector: Vector)

    fun addUserModeChangesListener(userModeChangesListener: UserModeChangesListener)

    fun removeUserModeChangesListener(toDelete: UserModeChangesListener)

    fun addUserModeChangesListenerAndNotify(userModeChangesListener: UserModeChangesListener)

    fun addBoardChangesListener(boardChangeListener: BoardChangesListener)

    fun removeBoardChangesListener(toDelete: BoardChangesListener)

    fun addBoardChangesListenerAndNotify(boardChangeListener: BoardChangesListener)

    fun addSuggestLineChangesListener(suggestLineChangesListener: SuggestLineChangesListener)

    fun removeSuggestLineChangesListener(suggestLineChangesListener: SuggestLineChangesListener)

    fun addSuggestLineChangesListenerAndNotify(suggestLineChangesListener: SuggestLineChangesListener)

    fun addUndoChangeShowButtonListener(undoChangeShowButtonListener: UndoChangeShowButtonListener)

    fun removeUndoChangeShowButtonListener(undoChangeShowButtonListener: UndoChangeShowButtonListener)

    fun addUndoChangeShowButtonListenerAndNotify(undoChangeShowButtonListener: UndoChangeShowButtonListener)

    fun addRedoChangeShowButtonListener(redoChangeShowButtonListener: RedoChangeShowButtonListener)

    fun removeRedoChangeShowButtonListener(redoChangeShowButtonListener: RedoChangeShowButtonListener)

    fun addRedoChangeShowButtonListenerAndNotify(redoChangeShowButtonListener: RedoChangeShowButtonListener)

    fun addTextUpdateListener(addTextUpdateListener: TextUpdateListener)

    fun removeTextUpdateListener(addTextUpdateListener: TextUpdateListener)

    fun addTextUpdateListenerAndNotify(addTextUpdateListener: TextUpdateListener)
}