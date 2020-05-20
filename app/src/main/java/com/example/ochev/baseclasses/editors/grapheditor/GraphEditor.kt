package com.example.ochev.baseclasses.editors.grapheditor

import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.FigureNormalizer
import com.example.ochev.baseclasses.dataclasses.*
import com.example.ochev.baseclasses.edgefigures.Edge
import com.example.ochev.baseclasses.editors.drawingfigureseditor.DrawingFiguresEditor
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor
import com.example.ochev.baseclasses.vertexfigures.VertexFigure

class GraphEditor(
    var graph: Graph = Graph(),
    val drawingEditor: DrawingFiguresEditor = DrawingFiguresEditor()
) {
    fun deleteFigure(figure: Figure) {
        when (figure) {
            is VertexFigure -> deleteVertex(figure)
            is Edge -> deleteEdge(figure)
        }
    }

    private fun deleteEdge(edgeFigure: Edge) {
        val newGraph = Graph()
        newGraph.figures.vertexes += graph.figures.vertexes

        graph.figures.edges.forEach {
            if (it.first != edgeFigure) {
                newGraph.figures.edges.add(it)
            }
        }

        graph = newGraph
        drawingEditor.deleteFigure(edgeFigure)
    }

    private fun deleteVertex(vertexFigure: VertexFigure) {
        val newGraph = Graph()

        graph.figures.vertexes.forEach {
            if (it.first != vertexFigure) newGraph.figures.vertexes.add(it)
        }

        graph.figures.edges.forEach {
            if (it.first.beginFigure != vertexFigure && it.first.endFigure != vertexFigure) {
                newGraph.figures.edges.add(it)
            } else {
                drawingEditor.deleteFigure(it.first)
            }
        }

        drawingEditor.deleteFigure(vertexFigure)
        graph = newGraph
    }

    fun recalcEdgeHeights() {
        val fastHeightAcces: HashMap<VertexFigure, Int> = HashMap()
        graph.figures.vertexes.forEach { fastHeightAcces[it.first] = it.second }

        val newGraph = Graph()
        newGraph.figures.vertexes += graph.figures.vertexes

        graph.figures.edges.forEach {
            newGraph.figures.edges.add(
                Pair(
                    it.first, kotlin.math.min(
                        fastHeightAcces[it.first.beginFigure]!!,
                        fastHeightAcces[it.first.endFigure]!!
                    )
                )
            )
        }

        graph = newGraph
    }

    fun modifyByStrokes(information: InformationForNormalizer) {
        val normalizer = FigureNormalizer()

        val result = normalizer.normaliseStrokes(information) ?: return

        when (result) {
            is VertexFigure -> graph.figures.addVertex(result, graph.figures.maxHeight + 1)
            is Edge -> graph.figures.addEdge(
                result,
                kotlin.math.min(
                    graph.figures.getHeight(result.beginFigure),
                    graph.figures.getHeight(result.endFigure)
                )
            )
        }

        drawingEditor.addFigure(result)
    }

    fun getFigureEditorByTouch(point: Point): VertexFigureEditor? {
        val bestFigure = graph.getFigureForEditing(point) ?: return null
        return when (bestFigure) {
            is VertexFigure ->
                VertexFigureEditor(InformationForVertexEditor(bestFigure, this))
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

        newGraph.figures.edges += reconnectEdges(linker)

        graph.figures.vertexes.forEach {
            if (it.first == old) newGraph.figures.addVertex(new, it.second)
            else newGraph.figures.vertexes.add(it)
        }

        graph = newGraph
        drawingEditor.changeFigure(old, new)
    }

    fun getLinker(changeFun: (VertexFigure) -> VertexFigure): HashMap<VertexFigure, VertexFigure> {
        val linker: HashMap<VertexFigure, VertexFigure> = HashMap()
        graph.figures.vertexes.forEach { linker[it.first] = changeFun(it.first) }
        return linker
    }

    fun reconnectEdges(linker: HashMap<VertexFigure, VertexFigure>): MutableList<Pair<Edge, Int>> {
        val result: MutableList<Pair<Edge, Int>> = ArrayList()
        graph.figures.edges.forEach {
            val newLine = Edge(
                linker[it.first.beginFigure]!!,
                linker[it.first.endFigure]!!
            )
            result.add(Pair(newLine, it.second))
            drawingEditor.changeFigure(it.first, newLine)
        }
        return result
    }

    fun moveVertexes(
        oldGraph: Graph,
        newGraph: Graph,
        linker: HashMap<VertexFigure, VertexFigure>
    ) {
        oldGraph.figures.vertexes.forEach {
            newGraph.figures.addVertex(linker[it.first]!!, it.second)
            drawingEditor.changeFigure(it.first, linker[it.first]!!)
        }
    }

    fun moveGraphByVector(vector: Vector) {
        val newGraph = Graph()
        val linker = getLinker { it.movedByVector(vector) }

        newGraph.figures.edges += reconnectEdges(linker)
        moveVertexes(graph, newGraph, linker)

        graph = newGraph
    }

    fun zoomByPointAndFactor(point: Point, factor: Float) {
        val newGraph = Graph()

        val linker = getLinker {
            it.movedByVector(Vector(point, it.center).multipliedByFloat(factor - 1f))
                .rescaledByFactor(factor)
        }

        newGraph.figures.edges += reconnectEdges(linker)
        moveVertexes(graph, newGraph, linker)

        graph = newGraph
    }

    fun clear() {
        graph = Graph()
        drawingEditor.clear()
    }


}