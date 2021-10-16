package com.example.ochev.baseclasses.editors.boardeditor

import android.content.Context
import android.graphics.Bitmap
import com.example.ochev.baseclasses.dataclasses.InformationForNormalizer
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.Vector
import com.example.ochev.baseclasses.editors.FigureEditor
import com.example.ochev.baseclasses.editors.edgeeditor.EdgeEditor
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureMover
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureShaper
import com.example.ochev.callbacks.BoardChangesListener
import com.example.ochev.callbacks.UserMode
import com.example.ochev.callbacks.UserModeChangesListener
import com.example.ochev.ml.Classifier
import java.util.concurrent.Executors

object ViewerFactory {
    fun create(context: Context): BoardViewer = BoardViewerImpl(context)
}

class BoardViewerImpl(context: Context) : BoardViewer {
    private val graphEditor = GraphEditor()
    private val classifier = Classifier(context)
    private val userModeChangesListeners = arrayListOf<UserModeChangesListener>()
    private val boardChangesListeners = arrayListOf<BoardChangesListener>()
    private var currentUserMode = UserMode.DRAWING

    init {
        classifier.initialize(Executors.newCachedThreadPool())
    }

    override fun moveBoard(vector: Vector) {
        graphEditor.moveGraphByVector(vector)
        notifyBoardChanges()
    }

    override fun createFigureByStrokes(bitmap: Bitmap, strokes: MutableList<Stroke>?): Boolean {
        val edited = graphEditor.modifyByStrokes(
            InformationForNormalizer(
                classifier = classifier,
                graphEditor = graphEditor,
                strokes = strokes,
                bitmap = bitmap
            )
        )
        return when (edited) {
            true -> {
                notifyBoardChanges()
                true
            }
            false -> false
        }
    }

    private inner class FiguresManipulatorImpl(private val id: Int) :
        BoardManipulator {
        private var figureEditor: FigureEditor? = null
        private var shaper: VertexFigureShaper? = null
        private var mover: VertexFigureMover? = null

        override fun deleteSelected() {
            graphEditor.deleteFigure(id)
            goToDrawingMode()
            notifyBoardChanges()
        }

        override fun copySelected() {
            graphEditor.copyFigure(id)
            goToDrawingMode()
            notifyBoardChanges()
        }

        override fun startEditing(pt: Point) {
            figureEditor = graphEditor.getFigureEditorByTouch(pt)
            shaper = (figureEditor as? VertexFigureEditor)?.shaper
            mover = (figureEditor as? VertexFigureEditor)?.mover
            if (shaper?.shapingBegins(pt) == false) {
                shaper = null
            }
            if (mover?.moveBegins(pt) == false) {
                mover = null
            }
            if (shaper != null) {
                mover = null
            }
        }

        override fun cancelEditing(pt: Point) {
            figureEditor = null
            mover?.moveEnds()
            shaper = null
            mover = null
        }

        override fun putPoint(pt: Point): BoardManipulator? {
            if (figureEditor == null) {
                goToDrawingMode()
                return null
            }
            when (figureEditor) {
                is VertexFigureEditor -> {
                    if (mover != null) {
                        mover!!.nextPoint(pt)
                    } else if (shaper != null) {
                        shaper!!.nextPoint(pt)
                    }
                }
                else -> {
                    return this
                }
            }
            notifyBoardChanges()

            return this
        }

        override fun currentEditingFigure(): Int {
            return id
        }
    }

    override fun selectFigureByPoint(point: Point): BoardManipulator? {
        val editor = graphEditor.getFigureEditorByTouch(point)
        if (editor == null) {
            goToDrawingMode()
            return null
        }
        goToEditingMode()
        return FiguresManipulatorImpl(editor.figureId)
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

    private fun notifyBoardChanges() {
        boardChangesListeners.forEach {
            it.onBoardChanged(graphEditor.allFiguresSortedByHeights)
        }
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
}