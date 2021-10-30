package com.example.ochev.baseclasses.cacheparser

import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.nodes.EdgeNode
import com.example.ochev.baseclasses.dataclasses.nodes.VertexFigureNode
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Circle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rectangle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rhombus
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor

/*
    сначала записываем количество фигур и счетчик, затем описание каждой фигуры.
    <edge> : FIGURE_ID.EDGE.ordinal, <vertex>, <vertex>
    <vertex> : FIGURE_ID._.ordinal, height, center, <circle> | <rectangle> | <rhombus>
    <circle> : radius
    <rentable> : leftDownCorner, rightUpCorner
    <rhombus> : leftCorner, upCorner

//        TODO(add texts?)
*/
object GraphWriter {

    fun write(graphEditor: GraphEditor, cacheParser: CacheParser) {
        cacheParser.writeInt(graphEditor.allFiguresSortedByHeights.size)
        cacheParser.writeInt(graphEditor.figureCounter)
        graphEditor.allFiguresSortedByHeights.forEach { figureNode ->
            figureNode.figure.getFigureId()
            when (figureNode) {
                is VertexFigureNode -> {
                    writeVertexFigure(figureNode, cacheParser)
                }
                is EdgeNode -> {
                    writeEdgeFigure(figureNode, cacheParser)
                }
            }
        }
    }

    private fun writePoint(pt: Point, cacheParser: CacheParser) {
        cacheParser.writeFloat(pt.x)
        cacheParser.writeFloat(pt.y)
    }

    private fun writeVertexFigure(figureNode: VertexFigureNode, cacheParser: CacheParser) {
        cacheParser.writeInt(figureNode.figure.getFigureId().order)
        cacheParser.writeInt(figureNode.height)
        writePoint(figureNode.figure.center, cacheParser)
        when (val figure = figureNode.figure) {
            is Circle -> {
                cacheParser.writeFloat(figure.radius)
            }
            is Rectangle -> {
                writePoint(figure.leftDownCorner, cacheParser)
                writePoint(figure.rightUpCorner, cacheParser)
            }
            is Rhombus -> {
                writePoint(figure.leftCorner, cacheParser)
                writePoint(figure.upCorner, cacheParser)
            }
        }
    }

    private fun writeEdgeFigure(figureNode: EdgeNode, cacheParser: CacheParser) {
        cacheParser.writeInt(figureNode.figure.getFigureId().order)
        writeVertexFigure(figureNode.figure.from, cacheParser)
        writeVertexFigure(figureNode.figure.to, cacheParser)
    }
}