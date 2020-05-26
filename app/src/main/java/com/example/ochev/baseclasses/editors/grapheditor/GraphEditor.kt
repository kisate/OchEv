package com.example.ochev.baseclasses.editors.grapheditor

import com.example.ochev.baseclasses.dataclasses.*
import com.example.ochev.baseclasses.dataclasses.edgefigures.Edge
import com.example.ochev.baseclasses.dataclasses.nodes.EdgeNode
import com.example.ochev.baseclasses.dataclasses.nodes.FigureNode
import com.example.ochev.baseclasses.dataclasses.nodes.VertexFigureNode
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure
import com.example.ochev.baseclasses.editors.FigureEditor
import com.example.ochev.baseclasses.editors.edgeeditor.EdgeEditor
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.baseclasses.normalizers.FigureNormalizer
import com.example.ochev.viewclasses.graphdrawers.drawinginformations.DrawingInformation
import com.example.ochev.viewclasses.graphdrawers.drawinginformations.DrawingMode

class GraphEditor(
    var graph: Graph = Graph(),
    var figureCounter: Int = 0
) {
    val history: GraphChangeHistory = GraphChangeHistory(graphEditor = this)

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

    fun deleteFigure(figure: Figure) {
        history.saveState()
        when (figure) {
            is VertexFigure -> deleteVertex(figure)
            is Edge -> deleteEdge(figure)
        }
    }

    private fun deleteEdge(edgeFigure: Edge) {
        val newGraph = Graph()
        newGraph.figures.vertices += graph.figures.vertices

        graph.figures.edges.forEach {
            if (it.figure != edgeFigure) {
                newGraph.figures.edges.add(it)
            }
        }

        graph = newGraph
    }

    private fun deleteVertex(vertexFigure: VertexFigure) {
        val newGraph = Graph()

        graph.figures.vertices.forEach {
            if (it.figure != vertexFigure) newGraph.figures.vertices.add(it)
        }

        graph.figures.edges.forEach {
            if (it.figure.beginFigure != vertexFigure && it.figure.endFigure != vertexFigure) {
                newGraph.figures.edges.add(it)
            }
        }

        graph = newGraph
    }


    fun modifyByStrokes(information: InformationForNormalizer): Boolean {
        val normalizer =
            FigureNormalizer()
        val result = normalizer.normaliseStrokes(information) ?: return false
        history.saveState()

        when (result) {
            is VertexFigure -> {
                graph.figures.vertices.add(
                    VertexFigureNode(
                        id = figureCounter++,
                        drawingInformation = DrawingInformation.getVertexDrawingInformation(result)!!,
                        height = graph.figures.maxHeight + 1,
                        figure = result
                    )
                )
            }
            is Edge -> {
                graph.figures.edges.add(
                    EdgeNode(
                        id = figureCounter++,
                        drawingInformation = DrawingInformation.getEdgeDrawingInformation()!!,
                        figure = result
                    )
                )
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

    fun replaceVertex(old: VertexFigure, new: VertexFigure) {
        // redirecting edges
        val newGraph = Graph()
        val linker = getLinker {
            if (it == old) new
            else it
        }

        reconnectEdges(newGraph, linker)

        graph.figures.vertices.forEach {
            if (it.figure == old) newGraph.figures.vertices.add(
                it.copy(figure = new)
            )
            else newGraph.figures.vertices.add(it)
        }

        graph = newGraph
    }

    fun getLinker(changeFun: (VertexFigure) -> VertexFigure): HashMap<VertexFigure, VertexFigure> {
        val linker: HashMap<VertexFigure, VertexFigure> = HashMap()
        graph.figures.vertices.forEach { linker[it.figure] = changeFun(it.figure) }
        return linker
    }

    fun reconnectEdges(
        newGraph: Graph,
        linker: HashMap<VertexFigure, VertexFigure>
    ) {
        graph.figures.edges.forEach {
            newGraph.figures.edges.add(
                it.copy(
                    figure = Edge(
                        linker[it.figure.beginFigure]!!,
                        linker[it.figure.endFigure]!!
                    )
                )
            )
        }
    }

    fun moveVertexes(
        newGraph: Graph,
        linker: HashMap<VertexFigure, VertexFigure>
    ) {

        graph.figures.vertices.forEach {
            newGraph.figures.vertices.add(it.copy(figure = linker[it.figure]!!))
        }
    }

    fun moveGraphByVector(vector: Vector) {
        history.moveByVector(vector)
        val newGraph = Graph()
        val linker = getLinker { it.movedByVector(vector) }

        reconnectEdges(newGraph, linker)
        moveVertexes(newGraph, linker)

        graph = newGraph
    }

    fun zoomByPointAndFactor(point: Point, factor: Float) {
        history.zoomByPointAndFactor(point, factor)
        val newGraph = Graph()

        val linker = getLinker {
            it.movedByVector(Vector(point, it.center).multipliedByFloat(factor - 1f))
                .rescaledByFactor(factor)
        }

        reconnectEdges(newGraph, linker)
        moveVertexes(newGraph, linker)

        graph = newGraph
    }

    fun clear() {
        history.saveState()
        graph = Graph()
    }

    fun getVertexFigureNodeByIdOrNull(id: Int): VertexFigureNode? {
        return graph.figures.vertices.firstOrNull { it.id == id }
    }

    fun getEdgeNodeByIdOrNull(id: Int): EdgeNode? {
        return graph.figures.edges.firstOrNull { it.id == id }
    }

    fun getFigureNodeByIdOrNull(id: Int): FigureNode? {
        return getVertexFigureNodeByIdOrNull(id) ?: getEdgeNodeByIdOrNull(id)
    }

    fun maximazeVertexHeightById(id: Int) {
        val vertex = getVertexFigureNodeByIdOrNull(id)!!
        graph.figures.vertices.remove(vertex)
        graph.figures.vertices.add(vertex.copy(height = graph.figures.maxHeight + 1))
    }


}