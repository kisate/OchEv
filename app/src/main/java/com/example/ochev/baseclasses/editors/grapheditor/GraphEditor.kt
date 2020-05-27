package com.example.ochev.baseclasses.editors.grapheditor

import com.example.ochev.baseclasses.dataclasses.*
import com.example.ochev.baseclasses.dataclasses.nodes.EdgeNode
import com.example.ochev.baseclasses.dataclasses.nodes.VertexFigureNode
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure
import com.example.ochev.baseclasses.editors.FigureEditor
import com.example.ochev.baseclasses.editors.edgeeditor.EdgeEditor
import com.example.ochev.baseclasses.editors.edgefigures.Edge
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.baseclasses.normalizers.FigureNormalizer
import com.example.ochev.viewclasses.drawers.drawinginformations.DrawingInformation
import com.example.ochev.viewclasses.drawers.drawinginformations.DrawingMode

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
        setDrawInfoToDefault()
    }

    fun undoRevertChange() {
        graph = history.undoRevert()
        setDrawInfoToDefault()
    }

    fun setDrawInfoToDefault() {
        graph.figures.figuresSortedByHeights.forEach {
            it.drawingInformation.enterMode(DrawingMode.DEFAULT)
        }
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
                    drawingInformation = DrawingInformation.getVertexDrawingInformation(result)!!,
                    height = graph.figures.maxHeight + 1,
                    figure = result
                )
                graph.addVertexNode(nodeToAdd)
                val vertexFigureEditor =
                    VertexFigureEditor(InformationForVertexEditor(nodeToAdd.id, this))
                vertexFigureEditor.mover.helper.tryToHelp()

            }
            is Edge -> {
                val nodeToAdd = EdgeNode(
                    id = figureCounter++,
                    drawingInformation = DrawingInformation.getEdgeDrawingInformation()!!,
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
                figure = vertexFigure,
                drawingInformation = DrawingInformation.getVertexDrawingInformation(vertexFigure)!!
            )
        )
    }

    private fun addEdge(edge: Edge) {
        graph.addEdgeNode(
            EdgeNode(
                id = figureCounter++,
                figure = edge,
                drawingInformation = DrawingInformation.getEdgeDrawingInformation()!!
            )
        )
    }

    fun deleteFigure(id: Int) {
        history.saveState()
        graph = graph.withDeletedFigure(id)
    }

    fun replaceVertex(id: Int, new: VertexFigure) {
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
        val vertex = graph.getVertexFigureNodeByIdOrNull(id)!!
        graph.figures.vertices.remove(vertex)
        graph.figures.vertices.add(vertex.copy(height = graph.figures.maxHeight + 1))
    }
}