package com.example.ochev.baseclasses.editors.grapheditor

import com.example.ochev.baseclasses.dataclasses.FigureContainer
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.Vector
import com.example.ochev.baseclasses.dataclasses.nodes.EdgeNode
import com.example.ochev.baseclasses.dataclasses.nodes.FigureNode
import com.example.ochev.baseclasses.dataclasses.nodes.VertexFigureNode
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure
import com.example.ochev.baseclasses.editors.edgefigures.Edge
import kotlin.math.abs


class Graph(
    val figures: FigureContainer = FigureContainer()
) {

    fun copy(): Graph {
        return Graph(figures.copy())
    }

    private fun getLinker(changeFun: (VertexFigureNode) -> VertexFigureNode): HashMap<VertexFigureNode, VertexFigureNode> {
        val linker: HashMap<VertexFigureNode, VertexFigureNode> = HashMap()
        figures.vertices.forEach { linker[it] = changeFun(it) }
        return linker
    }

    private fun reconnectEdges(
        newGraph: Graph,
        linker: HashMap<VertexFigureNode, VertexFigureNode>
    ) {
        figures.edges.forEach {
            newGraph.figures.edges.add(
                it.copy(
                    figure = Edge(
                        linker[it.figure.beginFigureNode]!!.id,
                        linker[it.figure.endFigureNode]!!.id,
                        it.figure.graphEditor
                    )
                )
            )
        }
    }

    private fun moveVertexes(
        newGraph: Graph,
        linker: HashMap<VertexFigureNode, VertexFigureNode>
    ) {
        figures.vertices.forEach {
            newGraph.figures.vertices.add(linker[it]!!)
        }
    }


    fun replacedVertex(id: Int, new: VertexFigure): Graph {
        val newGraph = Graph()
        val linker = getLinker {
            if (it.id != id) it
            else it.copy(figure = new)
        }


        reconnectEdges(newGraph, linker)

        figures.vertices.forEach {
            if (it.id == id) newGraph.figures.vertices.add(
                it.copy(figure = new)
            )
            else newGraph.figures.vertices.add(it)
        }
        return newGraph
    }


    fun withDeletedFigure(id: Int): Graph {
        val nodeToDelete = getFigureNodeByIdOrNull(id)
        return when (nodeToDelete) {
            is VertexFigureNode -> withDeletedVertex(id)
            is EdgeNode -> withDeletedEdge(id)
            else -> Graph()
        }
    }

    private fun withDeletedEdge(id: Int): Graph {
        val newGraph = Graph()
        newGraph.figures.vertices += figures.vertices
        figures.edges.forEach {
            if (it.id != id) {
                newGraph.figures.edges.add(it)
            }
        }

        return newGraph
    }

    fun withDeletedVertex(id: Int): Graph {
        val newGraph = Graph()

        figures.vertices.forEach {
            if (it.id != id) newGraph.figures.vertices.add(it)
        }

        figures.edges.forEach {
            if (it.figure.beginFigureNode.id != id && it.figure.endFigureNode.id != id) {
                newGraph.figures.edges.add(it)
            }
        }

        return newGraph
    }

    fun movedByVector(vector: Vector): Graph {
        val newGraph = Graph()
        val linker = getLinker { it.copy(figure = it.figure.movedByVector(vector)) }

        reconnectEdges(newGraph, linker)
        moveVertexes(newGraph, linker)

        return newGraph
    }

    fun zoomedByPointAndFactor(point: Point, factor: Float): Graph {
        val newGraph = Graph()

        val linker = getLinker {
            it.copy(
                figure = it.figure.movedByVector(
                    Vector(point, it.figure.center).multipliedByFloat(factor - 1f)
                )
                    .rescaledByFactor(factor)
            )
        }

        reconnectEdges(newGraph, linker)
        moveVertexes(newGraph, linker)

        return newGraph
    }

    fun addVertexNode(node: VertexFigureNode) {
        figures.vertices.add(node)
    }

    fun addEdgeNode(node: EdgeNode) {
        figures.edges.add(node)
    }

    fun getVertexFigureNodeByIdOrNull(id: Int): VertexFigureNode? {
        return figures.vertices.firstOrNull { it.id == id }
    }

    fun getEdgeNodeByIdOrNull(id: Int): EdgeNode? {
        return figures.edges.firstOrNull { it.id == id }
    }

    fun getFigureNodeByIdOrNull(id: Int): FigureNode? {
        return getVertexFigureNodeByIdOrNull(id) ?: getEdgeNodeByIdOrNull(id)
    }

    fun getGraphRestrictions(): MutableList<Float>? {
        val allPoints: MutableList<Point> = ArrayList()
        figures.vertices.forEach {
            allPoints += it.figure.importantPoints
        }
        if (allPoints.isEmpty()) return null
        return Stroke.getStrokesRestrictions(mutableListOf(Stroke(allPoints)))
    }

    fun getClosestToPointVertexFigureOrNull(point: Point): VertexFigureNode? {
        if (figures.vertices.isEmpty()) return null

        val bestDist = figures.vertices.minBy {
            it.figure.getDistanceToPointOrZeroIfInside(point)
        }!!.figure.getDistanceToPointOrZeroIfInside(point)

        val lookFor = figures.vertices.partition {
            abs(it.figure.getDistanceToPointOrZeroIfInside(point) - bestDist) <= 0.0001
        }.first
        return lookFor.maxBy { it.height }!!
    }

    fun getClosestToPointEdgeFigureOrNull(point: Point): EdgeNode? {
        return figures.edges.minBy {
            it.figure.getDistanceToPoint(point)
        }
    }

    fun getClosestToPointFigureOrNull(point: Point): FigureNode? {
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

    fun getFigureForEditing(point: Point): FigureNode? {
        val bestFigure = getClosestToPointFigureOrNull(point)
        return if (bestFigure == null || !bestFigure.figure.checkIfFigureIsCloseEnough(point)) null
        else bestFigure
    }
}