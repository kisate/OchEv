package com.example.ochev.baseclasses.editors.grapheditor

import com.example.ochev.baseclasses.EdgeFigure
import com.example.ochev.baseclasses.FigureNormalizer
import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.*
import com.example.ochev.baseclasses.edgefigures.Line
import com.example.ochev.baseclasses.editors.vertexeditor.VertexFigureEditor

class GraphEditor(
    var graph: Graph = Graph()
) {
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
            is EdgeFigure -> graph.figures.addEdge(
                result,
                kotlin.math.min(
                    graph.figures.getHeight(result.beginFigure),
                    graph.figures.getHeight(result.endFigure)
                )
            )
        }
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
    }

    fun getLinker(changeFun: (VertexFigure) -> VertexFigure): HashMap<VertexFigure, VertexFigure> {
        val linker: HashMap<VertexFigure, VertexFigure> = HashMap()
        graph.figures.vertexes.forEach { linker[it.first] = changeFun(it.first) }
        return linker
    }

    fun reconnectEdges(linker: HashMap<VertexFigure, VertexFigure>): MutableList<Pair<EdgeFigure, Int>> {
        val result: MutableList<Pair<EdgeFigure, Int>> = ArrayList()
        graph.figures.edges.forEach {
            when (it.first) {
                is Line -> result.add(
                    Pair(
                        Line(linker[it.first.beginFigure]!!, linker[it.first.endFigure]!!),
                        it.second
                    )
                )
            }
        }
        return result
    }

    fun moveGraphByVector(vector: Vector) {
        val newGraph = Graph()
        val linker = getLinker { it.movedByVector(vector) }
        val newEdges = reconnectEdges(linker)

        graph.figures.vertexes.forEach { newGraph.figures.addVertex(linker[it.first]!!, it.second) }
        newGraph.figures.edges += newEdges

        graph = newGraph
    }

}