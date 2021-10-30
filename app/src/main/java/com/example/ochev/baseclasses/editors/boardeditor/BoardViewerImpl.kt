package com.example.ochev.baseclasses.editors.boardeditor

import android.graphics.Bitmap
import android.util.Log
import com.example.ochev.baseclasses.cacheparser.CacheParser
import com.example.ochev.baseclasses.cacheparser.GraphReader
import com.example.ochev.baseclasses.cacheparser.GraphWriter
import com.example.ochev.baseclasses.dataclasses.*
import com.example.ochev.baseclasses.editors.FigureEditor
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureMover
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureShaper
import com.example.ochev.callbacks.*
import com.example.ochev.ml.Classifier
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object ViewerFactory {
    private val executorService: ExecutorService = Executors.newCachedThreadPool()

    fun create(classifier: Classifier): BoardViewer = BoardViewerImpl(classifier, executorService)

    fun create(classifier: Classifier, cacheParser: CacheParser): BoardViewer =
        BoardViewerImpl(classifier, executorService, cacheParser)
}

class BoardViewerImpl(
    private val classifier: Classifier,
    private val executorService: ExecutorService,
    cacheParser: CacheParser? = null
) : BoardViewer {
    private val graphEditor: GraphEditor
    init {
        classifier.initialize(Executors.newCachedThreadPool())
        graphEditor = if (cacheParser != null) {
            executorService.submit(
                MyJob(cacheParser)
            ).get()
        } else {
            GraphEditor()
        }
    }

    private inner class MyJob(private val cacheParser: CacheParser) : Callable<GraphEditor> {
        override fun call() = GraphReader.readGraph(cacheParser)
    }

    private var currentUserMode = UserMode.DRAWING

    private val userModeChangesListeners = arrayListOf<UserModeChangesListener>()
    private val boardChangesListeners = arrayListOf<BoardChangesListener>()
    private val suggestLineChangesListeners = arrayListOf<SuggestLineChangesListener>()

    override fun moveBoard(vector: Vector) {
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

    private fun isEditorCopyable(figureEditor: FigureEditor?): Boolean {
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

    override fun setLastEnterTimeMs(millis: Long) {

    }

    override fun getLastEnterTimeMs(): Long {
        return 0
    }

    override fun setWindowParams(height: Int, width: Int) {

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

    override fun saveInCache(cacheParser: CacheParser) {
        Log.d("ainur cache", "START SAVING")
        executorService.submit {
            GraphWriter.write(graphEditor, cacheParser)
        }
    }

    override fun scaleBoard(centre: Point, scaleValue: Float) {
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
        suggestLineChangesListeners.add(suggestLineChangesListener)
    }

    override fun removeSuggestLineChangesListener(suggestLineChangesListener: SuggestLineChangesListener) {
        suggestLineChangesListeners.remove(suggestLineChangesListener)
    }

    override fun addSuggestLineChangesListenerAndNotify(suggestLineChangesListener: SuggestLineChangesListener) {
        addSuggestLineChangesListener(suggestLineChangesListener)
//        suggestLineChangesListener.onSuggestLineChanged(TODO())
    }

    override fun addUndoChangeShowButtonListener(undoChangeShowButtonListener: UndoChangeShowButtonListener) {
//        TODO("Not yet implemented")
    }

    override fun removeUndoChangeShowButtonListener(undoChangeShowButtonListener: UndoChangeShowButtonListener) {
//        TODO("Not yet implemented")
    }

    override fun addUndoChangeShowButtonListenerAndNotify(undoChangeShowButtonListener: UndoChangeShowButtonListener) {
//        TODO("Not yet implemented")
    }

    override fun addRedoChangeShowButtonListener(redoChangeShowButtonListener: RedoChangeShowButtonListener) {
//        TODO("Not yet implemented")
    }

    override fun removeRedoChangeShowButtonListener(redoChangeShowButtonListener: RedoChangeShowButtonListener) {
//        TODO("Not yet implemented")
    }

    override fun addRedoChangeShowButtonListenerAndNotify(redoChangeShowButtonListener: RedoChangeShowButtonListener) {
//        TODO("Not yet implemented")
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
        val oldMode = currentUserMode
        currentUserMode = UserMode.DRAWING
        if (oldMode != currentUserMode)
            notifyUserModeChanges()
    }

    private fun goToEditingMode(isCopyable: Boolean) {
        val oldMode = currentUserMode
        currentUserMode =
            if (isCopyable) {
                UserMode.EDITING__COPY_ENABLED
            } else {
                UserMode.EDITING__COPY_DISABLED
            }
        if (oldMode != currentUserMode)
            notifyUserModeChanges()
    }

    private inner class FiguresManipulatorImpl(private var id: Int) :
        BoardManipulator {
        init {
            graphEditor.maximizeVertexHeightById(id)
            notifyBoardChanges()
        }

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
            val newId = (figureEditor as? VertexFigureEditor)?.figureId
            if (newId != null) {
                id = newId
            }
            goToEditingMode(isEditorCopyable(figureEditor))
            graphEditor.maximizeVertexHeightById(id)
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
    }

}