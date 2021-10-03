package com.example.ochev.baseclasses.editors.boardeditor

import android.content.Context
import android.graphics.Bitmap
import com.example.ochev.baseclasses.dataclasses.InformationForNormalizer
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor
import com.example.ochev.callbacks.BoardChangesListener
import com.example.ochev.callbacks.UserMode
import com.example.ochev.callbacks.UserModeChangesListener
import com.example.ochev.ml.Classifier

object ViewerFactory {
    fun create(context: Context): BoardViewer = BoardViewerImpl(context)
}

class BoardViewerImpl(context: Context) : BoardViewer {
    private val graphEditor = GraphEditor()
    private val classifier = Classifier(context)
    private val listeners = arrayListOf<UserModeChangesListener>()
    private val currentUserMode = UserMode.EDITING

    override fun createFigureByStrokes(bitmap: Bitmap, strokes: MutableList<Stroke>?): Boolean {
        return graphEditor.modifyByStrokes(
            InformationForNormalizer(
                classifier = classifier,
                graphEditor = graphEditor,
                strokes = strokes,
                bitmap = bitmap
            )
        )
    }

    override fun selectFigureByPoint(point: Point): BoardManipulator {
        TODO()
    }

    override fun addBoardChangesListener(boardChangeListener: BoardChangesListener) {
        TODO("Not yet implemented")
    }

    override fun addBoardChangesListenerAndNotify(boardChangeListener: BoardChangesListener) {
        TODO("Not yet implemented")
    }

    override fun clearBoard() {
        graphEditor.clear()
    }

    override fun saveToGallery() {
        TODO()
    }

    override fun undoChange() {
        graphEditor.revertChange()
    }

    override fun redoChange() {
        graphEditor.undoRevertChange()
    }

    override fun scaleBoard(centre: Point, scaleValue: Float) {
        graphEditor.zoomByPointAndFactor(centre, scaleValue)
    }

    override fun addListener(userModeChangesListener: UserModeChangesListener) {
        listeners.add(userModeChangesListener)
    }

    override fun addListenerAndNotify(userModeChangesListener: UserModeChangesListener) {
        addListener(userModeChangesListener)
        userModeChangesListener.onUserModeChanged(currentUserMode)
    }
}