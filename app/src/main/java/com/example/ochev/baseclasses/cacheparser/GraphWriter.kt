package com.example.ochev.baseclasses.cacheparser

import android.graphics.Bitmap
import android.util.Base64
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.nodes.EdgeNode
import com.example.ochev.baseclasses.dataclasses.nodes.VertexFigureNode
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Circle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rectangle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rhombus
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor
import java.io.ByteArrayOutputStream

/*
    записываем существование битмапы 0 | 1, битмапу если существует, количество фигур и счетчик, затем описание каждой фигуры.
    <edge> : FIGURE_ID.EDGE.ordinal, id, <vertex>, <vertex>
    <vertex> : FIGURE_ID._.ordinal, id, height, center, <circle> | <rectangle> | <rhombus>
    <circle> : radius
    <rentable> : leftDownCorner, rightUpCorner
    <rhombus> : leftCorner, upCorner

//        TODO(add texts?)
*/
object GraphWriter {
    fun bitmapToString(bitmap: Bitmap?): String? {
        if (bitmap == null)
            return null
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun write(graphEditor: GraphEditor, bitmap: Bitmap?, cacheParser: CacheParser) {
        val str: String? = bitmapToString(bitmap)
        if (bitmap == null || str == null) {
            cacheParser.writeInt(0)
        } else {
            cacheParser.writeInt(1)
            cacheParser.writeString(str)
        }
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
        cacheParser.closeTransaction()
    }

    private fun writePoint(pt: Point, cacheParser: CacheParser) {
        cacheParser.writeFloat(pt.x)
        cacheParser.writeFloat(pt.y)
    }

    private fun writeVertexFigure(figureNode: VertexFigureNode, cacheParser: CacheParser) {
        cacheParser.writeInt(figureNode.figure.getFigureId().order)
        cacheParser.writeInt(figureNode.id)
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
        cacheParser.writeInt(figureNode.id)
        writeVertexFigure(figureNode.figure.from, cacheParser)
        writeVertexFigure(figureNode.figure.to, cacheParser)
    }
}