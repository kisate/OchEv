package com.example.ochev.baseclasses.editors.grapheditor

import com.example.ochev.baseclasses.dataclasses.*
import com.example.ochev.baseclasses.dataclasses.nodes.EdgeNode
import com.example.ochev.baseclasses.dataclasses.nodes.FigureNode
import com.example.ochev.baseclasses.dataclasses.nodes.VertexFigureNode
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure
import com.example.ochev.baseclasses.editors.FigureEditor
import com.example.ochev.baseclasses.editors.edgeeditor.EdgeEditor
import com.example.ochev.baseclasses.editors.edgefigures.Edge
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.baseclasses.normalizers.FigureNormalizer

class GraphEditor(
    var graph: Graph = Graph(),
    var figureCounter: Int = 0
) {
    val history: GraphChangeHistory = GraphChangeHistory(graphEditor = this)
    val allFiguresSortedByHeights
        get() = graph.figures.figuresSortedByHeights
    val allVertexes
        get() = graph.figures.vertices
    val allEdges
        get() = graph.figures.edges

    fun revertChange() {
        graph = history.revert()
    }

    fun undoRevertChange() {
        graph = history.undoRevert()
    }

    fun isRevertible(): Boolean {
        return history.isRevertible()
    }

    fun isUndoRevertible(): Boolean {
        return history.isUndoRevertible()
    }

    fun getFigureNodeByIdOrNull(id: Int): FigureNode? {
        return graph.getFigureNodeByIdOrNull(id)
    }

    fun modifyByStrokes(information: InformationForNormalizer): Boolean {
        val normalizer =
            FigureNormalizer()
        val result = normalizer.normaliseStrokes(information) ?: return false

        history.saveState()

        when (result) {
            is VertexFigure -> {
                val nodeToAdd = VertexFigureNode(
                    id = figureCounter++,
                    height = graph.figures.maxHeight + 1,
                    figure = result
                )
                graph.addVertexNode(nodeToAdd)
            }
            is Edge -> {
                val nodeToAdd = EdgeNode(
                    id = figureCounter++,
                    figure = result
                )
                graph.addEdgeNode(nodeToAdd)
            }
        }
        return true
    }

    fun getFigureEditorByTouch(point: Point): FigureEditor? {
        val bestFigure = graph.getFigureForEditing(point) ?: return null
        return when (bestFigure) {
            is VertexFigureNode ->
                VertexFigureEditor(InformationForVertexEditor(bestFigure.id, this))
            is EdgeNode ->
                EdgeEditor(InformationForEdgeEditor(bestFigure.id, this))
            else -> null
        }
    }

    fun addFigure(figure: Figure) {
        history.saveState()
        when (figure) {
            is VertexFigure -> {
                addVertexFigure(figure)
            }
            is Edge -> {
                addEdge(figure)
            }
        }
    }

    private fun addVertexFigure(vertexFigure: VertexFigure) {
        graph.addVertexNode(
            VertexFigureNode(
                id = figureCounter++,
                height = graph.figures.maxHeight,
                figure = vertexFigure
            )
        )
    }

    private fun addEdge(edge: Edge) {
        graph.addEdgeNode(
            EdgeNode(
                id = figureCounter++,
                figure = edge
            )
        )
    }

    fun copyFigure(id: Int) {
        history.saveState()
        val editor = VertexFigureEditor(InformationForVertexEditor(id, this))
        editor.createCopy(editor.figureNode.figure.center)
    }

    fun deleteFigure(id: Int) {
        history.saveState()
        graph = graph.withDeletedFigure(id)
    }

    fun replaceVertex(id: Int, new: VertexFigure) {
        graph = graph.replacedVertex(id, new)
    }
    fun replaceVertex(id: Int, new: VertexFigureNode) {
        graph = graph.replacedVertex(id, new)
    }

    fun moveGraphByVector(vector: Vector) {
        history.moveByVector(vector)
        graph = graph.movedByVector(vector)
    }

    fun zoomByPointAndFactor(point: Point, factor: Float) {
        history.zoomByPointAndFactor(point, factor)
        graph = graph.zoomedByPointAndFactor(point, factor)
    }

    fun clear() {
        history.saveState()
        graph = Graph()
    }

    fun maximizeVertexHeightById(id: Int) {
        if (id == graph.figures.maxHeight) return
        val value = graph.figures.maxHeight + 1
        graph.figures.vertices.replaceAll {
            if (it.id != id) it
            else it.copy(height = value)
        }
    }
}