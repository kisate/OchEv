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
import com.example.ochev.viewclasses.drawers.drawinginformations.DrawingInformation
import com.example.ochev.viewclasses.drawers.drawinginformations.DrawingMode
import kotlin.math.abs

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
        val bestFigure = getFigureForEditing(point) ?: return null
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

    fun deleteFigure(figure: Figure) {
        history.saveState()
        graph = graph.withDeletedFigure(figure)
    }

    fun replaceVertex(old: VertexFigure, new: VertexFigure) {
        graph = graph.replacedVertex(old, new)
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

    fun getVertexFigureNodeByIdOrNull(id: Int): VertexFigureNode? {
        return graph.figures.vertices.firstOrNull { it.id == id }
    }

    fun getEdgeNodeByIdOrNull(id: Int): EdgeNode? {
        return graph.figures.edges.firstOrNull { it.id == id }
    }

    fun getFigureNodeByIdOrNull(id: Int): FigureNode? {
        return getVertexFigureNodeByIdOrNull(id) ?: getEdgeNodeByIdOrNull(id)
    }

    fun maximizeVertexHeightById(id: Int) {
        val vertex = getVertexFigureNodeByIdOrNull(id)!!
        graph.figures.vertices.remove(vertex)
        graph.figures.vertices.add(vertex.copy(height = graph.figures.maxHeight + 1))
    }

    fun getGraphRestrictions(): MutableList<Float>? {
        val allPoints: MutableList<Point> = ArrayList()
        allVertexes.forEach {
            allPoints += it.figure.importantPoints
        }
        if (allPoints.isEmpty()) return null
        return Stroke.getStrokesRestrictions(mutableListOf(Stroke(allPoints)))
    }

    private fun getFigureForEditing(point: Point): FigureNode? {
        val bestFigure = getClosestToPointFigureOrNull(point)
        return if (bestFigure == null || !bestFigure.figure.checkIfFigureIsCloseEnough(point)) null
        else bestFigure
    }


    private fun getClosestToPointVertexFigureOrNull(point: Point): VertexFigureNode? {
        if (allVertexes.isEmpty()) return null

        val bestDist = allVertexes.minBy {
            it.figure.getDistanceToPointOrZeroIfInside(point)
        }!!.figure.getDistanceToPointOrZeroIfInside(point)

        val lookFor = allVertexes.partition {
            abs(it.figure.getDistanceToPointOrZeroIfInside(point) - bestDist) <= 0.0001
        }.first
        return lookFor.maxBy { it.height }!!
    }

    private fun getClosestToPointEdgeFigureOrNull(point: Point): EdgeNode? {
        return allEdges.minBy {
            it.figure.getDistanceToPoint(point)
        }
    }

    private fun getClosestToPointFigureOrNull(point: Point): FigureNode? {
        val bestVertex = getClosestToPointVertexFigureOrNull(point)
        val bestEdge = getClosestToPointEdgeFigureOrNull(point)

        if (bestVertex == null) return bestEdge
        if (bestEdge == null) return bestVertex

        if (bestVertex.figure.checkIfPointIsInside(point)) return bestVertex

        return if (bestVertex.figure.getDistanceToPointOrZeroIfInside(point)
            <= bestEdge.figure.getDistanceToPoint(point)
        ) bestVertex
        else bestEdge
    }
}