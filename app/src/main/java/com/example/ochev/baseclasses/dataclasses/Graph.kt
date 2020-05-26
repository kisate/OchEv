package com.example.ochev.baseclasses.dataclasses

import com.example.ochev.baseclasses.dataclasses.edgefigures.Edge
import com.example.ochev.baseclasses.dataclasses.nodes.EdgeNode
import com.example.ochev.baseclasses.dataclasses.nodes.FigureNode
import com.example.ochev.baseclasses.dataclasses.nodes.VertexFigureNode
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure
import kotlin.math.abs


data class Graph(
    val figures: FigureContainer = FigureContainer()
) {


    fun copy(): Graph {
        return Graph(figures.copy())
    }


    fun getFigureForEditing(point: Point): FigureNode? {
        val bestFigure = getClosestToPointFigureOrNull(point)
        return if (bestFigure == null || !bestFigure.figure.checkIfFigureIsCloseEnough(point)) null
        else bestFigure
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

    fun replacedVertex(old: VertexFigure, new: VertexFigure): Graph {
        // redirecting edges
        val newGraph = Graph()
        val linker = getLinker {
            if (it == old) new
            else it
        }

        reconnectEdges(newGraph, linker)

        figures.vertices.forEach {
            if (it.figure == old) newGraph.figures.vertices.add(
                it.copy(figure = new)
            )
            else newGraph.figures.vertices.add(it)
        }

        return newGraph
    }

    private fun getLinker(changeFun: (VertexFigure) -> VertexFigure): HashMap<VertexFigure, VertexFigure> {
        val linker: HashMap<VertexFigure, VertexFigure> = HashMap()
        figures.vertices.forEach { linker[it.figure] = changeFun(it.figure) }
        return linker
    }

    private fun reconnectEdges(
        newGraph: Graph,
        linker: HashMap<VertexFigure, VertexFigure>
    ) {
        figures.edges.forEach {
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

    private fun moveVertexes(
        newGraph: Graph,
        linker: HashMap<VertexFigure, VertexFigure>
    ) {
        figures.vertices.forEach {
            newGraph.figures.vertices.add(it.copy(figure = linker[it.figure]!!))
        }
    }

    fun movedByVector(vector: Vector): Graph {
        val newGraph = Graph()
        val linker = getLinker { it.movedByVector(vector) }

        reconnectEdges(newGraph, linker)
        moveVertexes(newGraph, linker)

        return newGraph
    }

    fun zoomedByPointAndFactor(point: Point, factor: Float): Graph {
        val newGraph = Graph()

        val linker = getLinker {
            it.movedByVector(Vector(point, it.center).multipliedByFloat(factor - 1f))
                .rescaledByFactor(factor)
        }

        reconnectEdges(newGraph, linker)
        moveVertexes(newGraph, linker)

        return newGraph
    }

    fun withDeletedFigure(figure: Figure): Graph {
        return when (figure) {
            is VertexFigure -> withDeletedVertex(figure)
            is Edge -> withDeletedEdge(figure)
            else -> Graph()
        }
    }

    private fun withDeletedEdge(edgeFigure: Edge): Graph {
        val newGraph = Graph()
        newGraph.figures.vertices += figures.vertices
        figures.edges.forEach {
            if (it.figure != edgeFigure) {
                newGraph.figures.edges.add(it)
            }
        }

        return newGraph
    }

    fun withDeletedVertex(vertexFigure: VertexFigure): Graph {
        val newGraph = Graph()

        figures.vertices.forEach {
            if (it.figure != vertexFigure) newGraph.figures.vertices.add(it)
        }

        figures.edges.forEach {
            if (it.figure.beginFigure != vertexFigure && it.figure.endFigure != vertexFigure) {
                newGraph.figures.edges.add(it)
            }
        }

        return newGraph
    }

    fun addVertexNode(node: VertexFigureNode) {
        figures.vertices.add(node)
    }

    fun addEdgeNode(node: EdgeNode) {
        figures.edges.add(node)
    }
}