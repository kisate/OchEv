package com.example.ochev.baseclasses.editors.boardeditor

import android.graphics.Bitmap
import com.example.ochev.baseclasses.dataclasses.InformationForNormalizer
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.Vector
import com.example.ochev.baseclasses.editors.FigureEditor
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureMover
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureShaper
import com.example.ochev.callbacks.BoardChangesListener
import com.example.ochev.callbacks.SuggestLineChangesListener
import com.example.ochev.callbacks.UserMode
import com.example.ochev.callbacks.UserModeChangesListener
import com.example.ochev.ml.Classifier
import java.util.concurrent.Executors

object ViewerFactory {
    fun create(classifier: Classifier): BoardViewer = BoardViewerImpl(classifier)
}

class BoardViewerImpl(private val classifier: Classifier) : BoardViewer {
    init {
        classifier.initialize(Executors.newCachedThreadPool())
    }

    private val graphEditor = GraphEditor()
    private var currentUserMode = UserMode.DRAWING

    private val userModeChangesListeners = arrayListOf<UserModeChangesListener>()
    private val boardChangesListeners = arrayListOf<BoardChangesListener>()
    private val suggestLineChangesListener = arrayListOf<SuggestLineChangesListener>()

    override fun moveBoard(vector: Vector) {
        goToDrawingMode()
        graphEditor.moveGraphByVector(vector)
        notifyBoardChanges()
    }

    override fun createFigureByStrokes(bitmap: Bitmap, strokes: MutableList<Stroke>?): Boolean {
        goToDrawingMode()
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

    private fun isEditorCopyable(figureEditor: FigureEditor): Boolean {
        return when (figureEditor) {
            is VertexFigureEditor -> true
            else -> false
        }
    }

    override fun selectFigureByPoint(point: Point): BoardManipulator? {
        val editor = graphEditor.getFigureEditorByTouch(point)
        if (editor == null) {
            goToDrawingMode()
            return null
        }
        goToEditingMode(isEditorCopyable(editor))
        return FiguresManipulatorImpl(editor.figureId)
    }


    override fun clearBoard() {
        goToDrawingMode()
        graphEditor.clear()
        notifyBoardChanges()
    }

    override fun undoChange() {
        goToDrawingMode()
        graphEditor.revertChange()
        notifyBoardChanges()
    }

    override fun redoChange() {
        goToDrawingMode()
        graphEditor.undoRevertChange()
        notifyBoardChanges()
    }

    override fun scaleBoard(centre: Point, scaleValue: Float) {
        goToDrawingMode()
        graphEditor.zoomByPointAndFactor(centre, scaleValue)
        notifyBoardChanges()
    }

    override fun addUserModeChangesListener(userModeChangesListener: UserModeChangesListener) {
        userModeChangesListeners.add(userModeChangesListener)
    }

    override fun addUserModeChangesListenerAndNotify(userModeChangesListener: UserModeChangesListener) {
        addUserModeChangesListener(userModeChangesListener)
        userModeChangesListener.onUserModeChanged(currentUserMode)
    }

    override fun removeUserModeChangesListener(toDelete: UserModeChangesListener) {
        userModeChangesListeners.remove(toDelete)
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

    override fun addSuggestLineChangesListener(suggestLineChangesListener: SuggestLineChangesListener) {
        TODO("Not yet implemented")
    }

    override fun removeSuggestLineChangesListener(suggestLineChangesListener: SuggestLineChangesListener) {
        TODO("Not yet implemented")
    }

    override fun addSuggestLineChangesListenerAndNotify(suggestLineChangesListener: SuggestLineChangesListener) {
        TODO("Not yet implemented")
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

    private fun goToEditingMode(isCopyable: Boolean) {
        currentUserMode =
            if (isCopyable) {
                UserMode.EDITING__COPY_ENABLED
            } else {
                UserMode.EDITING__COPY_DISABLED
            }
        notifyUserModeChanges()
    }

    private inner class FiguresManipulatorImpl(private val id: Int) :
        BoardManipulator {
        private var figureEditor: FigureEditor? = null
        private var shaper: VertexFigureShaper? = null
        private var mover: VertexFigureMover? = null

        override fun deleteSelected() {
            goToDrawingMode()
            graphEditor.deleteFigure(id)
            notifyBoardChanges()
        }

        override fun copySelected() {
            graphEditor.copyFigure(id)
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
            notifyBoardChanges()
        }

        override fun cancelEditing(pt: Point) {
            figureEditor = null
            mover?.moveEnds()
            shaper = null
            mover = null
            notifyBoardChanges()
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

}