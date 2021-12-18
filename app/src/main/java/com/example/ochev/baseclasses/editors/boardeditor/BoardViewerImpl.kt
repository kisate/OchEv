package com.example.ochev.baseclasses.editors.boardeditor

import android.graphics.Bitmap
import android.util.Log
import com.example.ochev.baseclasses.cacheparser.CacheParser
import com.example.ochev.baseclasses.cacheparser.GraphReader
import com.example.ochev.baseclasses.cacheparser.GraphWriter
import com.example.ochev.baseclasses.dataclasses.*
import com.example.ochev.baseclasses.dataclasses.nodes.FigureNode
import com.example.ochev.baseclasses.dataclasses.nodes.VertexFigureNode
import com.example.ochev.baseclasses.editors.FigureEditor
import com.example.ochev.baseclasses.editors.edgeeditor.EdgeEditor
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureMover
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureShaper
import com.example.ochev.baseclasses.svgexporter.SvgExporter
import com.example.ochev.callbacks.*
import com.example.ochev.ml.Classifier
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
    private var graphEditor = GraphEditor()
    private var graphBitmap: Bitmap? = null

    init {
        classifier.initialize(Executors.newCachedThreadPool())
//        if (cacheParser != null) {
//            executorService.submit {
//                val pair = GraphReader.readGraph(cacheParser)
//                graphBitmap = pair.first
//                graphEditor = pair.second
//            }
//        }
    }

    private var lastManipulator: BoardManipulator? = null
    private var currentUserMode = UserMode()

    private val userModeChangesListeners = arrayListOf<UserModeChangesListener>()
    private val boardChangesListeners = arrayListOf<BoardChangesListener>()
    private val suggestLineChangesListeners = arrayListOf<SuggestLineChangesListener>()
    private val undoChangeShowButtonListeners = arrayListOf<UndoChangeShowButtonListener>()
    private val redoChangeShowButtonListeners = arrayListOf<RedoChangeShowButtonListener>()
    private val fontSizeListeners = arrayListOf<FontSizeListener>()

    private var height: Int = 0
    private var width: Int = 0
    private var lastEnterTime: Long = 0L

    override fun moveBoard(vector: Vector) {
        graphEditor.moveGraphByVector(vector)
        makeAllChanged()
        notifyBoardChanges()
    }

    override fun onDestroy() {
        userModeChangesListeners.clear()
        boardChangesListeners.clear()
        suggestLineChangesListeners.clear()
        redoChangeShowButtonListeners.clear()
        undoChangeShowButtonListeners.clear()
        fontSizeListeners.clear()
    }

    override fun getLastManipulator(): BoardManipulator? {
        return lastManipulator
    }

    override fun setGraphBitmap(bitmap: Bitmap) {
        graphBitmap = bitmap
    }

    override fun getGraphBitmap(): Bitmap? {
        return graphBitmap
    }

    override fun toSvg(): String {
        return SvgExporter.toSvg(graphEditor, height, width)
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
        goToEditingMode(isEditorCopyable(editor), isFontChangeAllowed(editor))
        lastManipulator = FiguresManipulatorImpl(editor.figureId)
        return lastManipulator
    }

    private fun isFontChangeAllowed(editor: FigureEditor?): Boolean {
        if (editor == null) return false
        if (editor is EdgeEditor) return false
        editor as VertexFigureEditor
        return !editor.figureNode.textInfo.text.isBlank()
    }

    override fun setLastEnterTimeMs(millis: Long) {
        lastEnterTime = millis
    }

    override fun getLastEnterTimeMs(): Long {
        return lastEnterTime
    }

    override fun setWindowParams(height: Int, width: Int) {
        this.height = height
        this.width = width
    }

    override fun clearBoard() {
        goToDrawingMode()
        graphEditor.clear()
        notifyBoardChanges()
    }

    override fun undoChange() {
        goToDrawingMode()
        graphEditor.revertChange()
        makeAllChanged()
        notifyBoardChanges()
    }

    override fun redoChange() {
        goToDrawingMode()
        graphEditor.undoRevertChange()
        makeAllChanged()
        notifyBoardChanges()
    }

    override fun saveInCache(cacheParser: CacheParser) {
        Log.d("ainur cache", "START SAVING")
//        executorService.submit {
//            GraphWriter.write(graphEditor, graphBitmap, cacheParser)
//        }
    }

    override fun scaleBoard(centre: Point, scaleValue: Float) {
        graphEditor.zoomByPointAndFactor(centre, scaleValue)
        makeAllChanged()
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
//      TODO("is this function needed?")
    }

    override fun addUndoChangeShowButtonListener(undoChangeShowButtonListener: UndoChangeShowButtonListener) {
        undoChangeShowButtonListeners.add(undoChangeShowButtonListener)
    }

    override fun removeUndoChangeShowButtonListener(undoChangeShowButtonListener: UndoChangeShowButtonListener) {
        undoChangeShowButtonListeners.remove(undoChangeShowButtonListener)
    }

    override fun addUndoChangeShowButtonListenerAndNotify(undoChangeShowButtonListener: UndoChangeShowButtonListener) {
        addUndoChangeShowButtonListener(undoChangeShowButtonListener)
        undoChangeShowButtonListener.onUndoChangeShowButtonChanged(graphEditor.isRevertible())
    }

    override fun addRedoChangeShowButtonListener(redoChangeShowButtonListener: RedoChangeShowButtonListener) {
        redoChangeShowButtonListeners.add(redoChangeShowButtonListener)
    }

    override fun removeRedoChangeShowButtonListener(redoChangeShowButtonListener: RedoChangeShowButtonListener) {
        redoChangeShowButtonListeners.remove(redoChangeShowButtonListener)
    }

    override fun addRedoChangeShowButtonListenerAndNotify(redoChangeShowButtonListener: RedoChangeShowButtonListener) {
        addRedoChangeShowButtonListener(redoChangeShowButtonListener)
        redoChangeShowButtonListener.onRedoChangeShowButtonListener(graphEditor.isUndoRevertible())
    }

    override fun addFontSizeListener(fontSizeListener: FontSizeListener) {
        fontSizeListeners.add(fontSizeListener)
    }

    override fun removeFondSizeListener(fontSizeListener: FontSizeListener) {
        fontSizeListeners.remove(fontSizeListener)
    }

    private fun notifyBoardChanges() {
        notifyRedoShowButtonChanges()
        notifyUndoShowButtonChanges()
        boardChangesListeners.forEach {
            it.onBoardChanged(graphEditor.allFiguresSortedByHeights)
        }
    }

    private var lastNotifyUndoShowButtonChange: Boolean? = null
    private fun notifyUndoShowButtonChanges() {
        if (graphEditor.isRevertible() == lastNotifyUndoShowButtonChange)
            return
        undoChangeShowButtonListeners.forEach {
            it.onUndoChangeShowButtonChanged(graphEditor.isRevertible())
        }
        lastNotifyUndoShowButtonChange = graphEditor.isRevertible()
    }

    private var lastNotifySuggestLineChange: List<LineSegment> = listOf()
    private fun notifySuggestLineChanges(lines: List<LineSegment>) {
        if (lines == lastNotifySuggestLineChange)
            return
        suggestLineChangesListeners.forEach {
            it.onSuggestLineChanged(lines)
        }
        lastNotifySuggestLineChange = lines
    }

    private var lastNotifyRedoShowButtonChange: Boolean? = null
    private fun notifyRedoShowButtonChanges() {
        if (lastNotifyRedoShowButtonChange == graphEditor.isUndoRevertible())
            return
        redoChangeShowButtonListeners.forEach {
            it.onRedoChangeShowButtonListener(graphEditor.isUndoRevertible())
        }
        lastNotifyRedoShowButtonChange = graphEditor.isUndoRevertible()
    }

    private var lastUserModeChange: UserMode = currentUserMode.copy()
    private fun notifyUserModeChanges() {
        if (lastUserModeChange == currentUserMode)
            return
        userModeChangesListeners.forEach { it.onUserModeChanged(currentUserMode) }
        lastUserModeChange = currentUserMode.copy()
    }

    private fun notifyInitialFont(fontSize: Int?) {
        if (fontSize == null) return
        fontSizeListeners.forEach {
            it.onInitialFontChanged(fontSize)
        }
    }

    private fun makeAllChanged() {
        val ids = graphEditor.allVertexes.map {
            it.id
        }
        ids.forEach { id ->
            val vertex = graphEditor.getFigureNodeByIdOrNull(id)
            assert(vertex is VertexFigureNode)
            assert(vertex != null)
            vertex as VertexFigureNode
            graphEditor.replaceVertex(
                id,
                vertex.copy(textInfo = vertex.textInfo.update(vertex.figure))
            )
        }
    }

    private fun goToDrawingMode() {
        val oldMode = currentUserMode.copy()
        currentUserMode.isEditing = false
        currentUserMode.isCopyEnabled = false
        currentUserMode.isFontChangeEnabled = false
        lastManipulator = null
        if (oldMode != currentUserMode)
            notifyUserModeChanges()
    }

    private fun goToEditingMode(isCopyEnabled: Boolean, isFontChangeEnabled: Boolean) {
        val oldMode = currentUserMode.copy()
        currentUserMode.isEditing = true
        currentUserMode.isCopyEnabled = isCopyEnabled
        currentUserMode.isFontChangeEnabled = isFontChangeEnabled
        if (oldMode != currentUserMode)
            notifyUserModeChanges()
    }

    private inner class FiguresManipulatorImpl(private var id: Int) :
        BoardManipulator {
        init {
            graphEditor.maximizeVertexHeightById(id)
            notifyInitialFont((graphEditor.getFigureNodeByIdOrNull(id) as? VertexFigureNode)?.textInfo?.fontSize)
            notifyBoardChanges()
        }

        private var figureEditor: FigureEditor? = null
        private var shaper: VertexFigureShaper? = null
        private var mover: VertexFigureMover? = null

        override fun getId(): Int {
            return id
        }

        override fun putText(text: String) {
            graphEditor.history.saveState()
            val vertex = graphEditor.getFigureNodeByIdOrNull(id)
            assert(vertex is VertexFigureNode)
            assert(vertex != null)
            vertex as VertexFigureNode
            graphEditor.replaceVertex(
                id,
                vertex.copy(textInfo = vertex.textInfo.update(vertex.figure).copy(text = text))
            )
            goToEditingMode(true, text.isNotBlank())
            notifyBoardChanges()
        }

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
            goToEditingMode(isEditorCopyable(figureEditor), isFontChangeAllowed(figureEditor))
            graphEditor.maximizeVertexHeightById(id)
            notifyBoardChanges()
        }

        override fun figureDrawn() {
            val vertex = graphEditor.getFigureNodeByIdOrNull(id)
            if (vertex !is VertexFigureNode) return
            graphEditor.replaceVertex(
                id,
                vertex.copy(textInfo = vertex.textInfo.copy(changed = false))
            )
        }

        override fun setFontSize(fontSize: Int) {
            graphEditor.history.saveState()
            val vertex = graphEditor.getFigureNodeByIdOrNull(id)
            assert(vertex is VertexFigureNode)
            assert(vertex != null)
            vertex as VertexFigureNode
            graphEditor.replaceVertex(
                id,
                vertex.copy(textInfo = vertex.textInfo.update(vertex.figure).copy(fontSize = fontSize))
            )
            notifyBoardChanges()
        }

        override fun getCurrentText(): String {
            val vertex = graphEditor.getFigureNodeByIdOrNull(id)
            assert(vertex is VertexFigureNode)
            assert(vertex != null)
            vertex as VertexFigureNode
            return vertex.textInfo.text
        }

        override fun startFontSizeChanging() {
            TODO("Not yet implemented")
        }

        override fun finishFontSizeChanging() {
            TODO("Not yet implemented")
        }

        override fun cancelEditing(pt: Point) {
            figureEditor = null
            val direction = mover?.moveEndsAt()
            shaper = null
            notifySuggestLineChanges(listOf())
            if (mover != null && direction != null) {
                mover!!.helper.editor.changeFigure(
                    mover!!.helper.editor.currentFigureState.movedByVector(
                        direction
                    )
                )
            }
            notifyBoardChanges()
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
                        notifySuggestLineChanges(mover!!.nextPoint(pt))
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