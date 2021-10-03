package com.example.ochev.baseclasses.editors.boardeditor

import android.content.Context
import android.graphics.Bitmap
import com.example.ochev.baseclasses.dataclasses.InformationForNormalizer
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.Vector
import com.example.ochev.baseclasses.editors.edgeeditor.EdgeEditor
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.callbacks.BoardChangesListener
import com.example.ochev.callbacks.UserMode
import com.example.ochev.callbacks.UserModeChangesListener
import com.example.ochev.ml.Classifier

object ViewerFactory {
    fun create(context: Context): BoardViewer = BoardViewerImpl(context)
}

class FiguresManipulatorImpl(private val id: Int, private val graphEditor: GraphEditor) : BoardManipulator {
    private var isActive = true;

    override fun putPoint(pt: Point): BoardManipulator? {
        val editor = graphEditor.getFigureEditorByTouch(pt)
        if (editor == null) {
            isActive = false
            return null
        }
        TODO()
    }

    override fun actionIsOver(): Boolean {
        return isActive
    }

    override fun currentEditingFigure(): Int {
        return id
    }
}

class BoardViewerImpl(context: Context) : BoardViewer {
    private val graphEditor = GraphEditor()
    private val classifier = Classifier(context)
    private val userModeChangesListeners = arrayListOf<UserModeChangesListener>()
    private val boardChangesListeners = arrayListOf<BoardChangesListener>()
    private var currentUserMode = UserMode.DRAWING

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

    override fun moveBoard(vector: Vector) {
        graphEditor.moveGraphByVector(vector)
    }

    private fun notifyUserModeChanges() {
        userModeChangesListeners.forEach { it.onUserModeChanged(currentUserMode) }
    }

    private fun goToDrawingMode() {
        currentUserMode = UserMode.DRAWING
        notifyUserModeChanges()
    }

    private fun goToEditingMode() {
        currentUserMode = UserMode.EDITING
        notifyUserModeChanges()
    }

    override fun selectFigureByPoint(point: Point): BoardManipulator? {
        val editor = graphEditor.getFigureEditorByTouch(point)
        if (editor == null) {
            goToDrawingMode()
            return null
        }
        goToEditingMode()
        return FiguresManipulatorImpl(editor.figureId, graphEditor)
    }

    override fun addBoardChangesListener(boardChangeListener: BoardChangesListener) {
        boardChangesListeners.add(boardChangeListener)
    }

    override fun removeBoardChangesListener(toDelete: BoardChangesListener) {
        boardChangesListeners.remove(toDelete)
    }

    override fun addBoardChangesListenerAndNotify(boardChangeListener: BoardChangesListener) {
        addBoardChangesListener(boardChangeListener)
        boardChangeListener.onBoardChanged(graphEditor.allFiguresSortedByHeights)
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
        userModeChangesListeners.add(userModeChangesListener)
    }

    override fun removeListener(toDelete: UserModeChangesListener) {
        userModeChangesListeners.remove(toDelete)
    }

    override fun addListenerAndNotify(userModeChangesListener: UserModeChangesListener) {
        addListener(userModeChangesListener)
        userModeChangesListener.onUserModeChanged(currentUserMode)
    }
}