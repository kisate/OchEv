package com.example.ochev.baseclasses.cacheparser

import com.example.ochev.baseclasses.dataclasses.FIGURE_ID
import com.example.ochev.baseclasses.dataclasses.FigureContainer
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.nodes.EdgeNode
import com.example.ochev.baseclasses.dataclasses.nodes.VertexFigureNode
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Circle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rectangle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rhombus
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure
import com.example.ochev.baseclasses.editors.edgefigures.Edge
import com.example.ochev.baseclasses.editors.grapheditor.Graph
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor

object GraphReader {
    fun readGraph(cacheParser: CacheParser): GraphEditor {
        val figureContainer = FigureContainer()
        val countOfFigures = cacheParser.readInt()
        val counter = cacheParser.readInt()
        repeat(countOfFigures) {
            val figureType = readFigureType(cacheParser)
            when (figureType) {
                FIGURE_ID.EDGE -> {
                    figureContainer.edges.add(readEdge(cacheParser))
                }
                else -> {
                    figureContainer.vertices.add(readVertex(figureType, cacheParser))
                }
            }
        }
        val graph = Graph(figureContainer)
        return GraphEditor(graph, counter)
    }

    private fun readFigureType(cacheParser: CacheParser): FIGURE_ID {
        return FIGURE_ID.values()[cacheParser.readInt()]
    }

    private fun readEdge(cacheParser: CacheParser): EdgeNode {
        val id = cacheParser.readInt()
        val v1 = readVertex(cacheParser)
        val v2 = readVertex(cacheParser)
        return EdgeNode(id = id, figure = Edge(v1, v2))
    }

    private fun readPoint(cacheParser: CacheParser): Point {
        return Point(cacheParser.readFloat(), cacheParser.readFloat())
    }

    private fun readVertex(figureType: FIGURE_ID, cacheParser: CacheParser): VertexFigureNode {
        val id = cacheParser.readInt()
        val height = cacheParser.readInt()
        val center = readPoint(cacheParser)
        var figure: VertexFigure
        when (figureType) {
            FIGURE_ID.CIRCLE -> {
                val radius = cacheParser.readFloat()
                figure = Circle(center, radius)
            }
            FIGURE_ID.RHOMBUS -> {
                val leftCorner = readPoint(cacheParser)
                val upCorner = readPoint(cacheParser)
                figure = Rhombus(leftCorner, upCorner)
            }
            FIGURE_ID.RECTANGLE -> {
                val leftDownCorner = readPoint(cacheParser)
                val rightUpCorner = readPoint(cacheParser)
                figure = Rectangle(leftDownCorner, rightUpCorner)
            }
            else -> {
                throw Exception("vertex reading went wrong")
            }
        }
        return VertexFigureNode(id, figure, height)
    }

    private fun readVertex(cacheParser: CacheParser): VertexFigureNode {
        return readVertex(readFigureType(cacheParser), cacheParser)
    }
}