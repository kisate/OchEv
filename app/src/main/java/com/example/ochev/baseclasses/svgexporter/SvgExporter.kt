package com.example.ochev.baseclasses.svgexporter

import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Circle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rectangle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rhombus
import com.example.ochev.baseclasses.editors.edgefigures.Edge
import com.example.ochev.baseclasses.editors.grapheditor.Graph
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor

object SvgExporter {
    fun toSvg(graph: GraphEditor, height: Int, width: Int): String {
        return toSvg(graph.graph, height, width)
    }

    private fun toSvg(graph: Graph, height: Int, width: Int): String {
        var res =
            "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"$width\" height=\"$height\" version=\"1.1\">"
        graph.figures.figuresSortedByHeights.forEach { figureNode ->
            when (val figure = figureNode.figure) {
                is Circle -> {
                    res += toSvg(figure)
                }
                is Rectangle -> {
                    res += toSvg(figure.importantPoints)
                }
                is Rhombus -> {
                    res += toSvg(figure.importantPoints)
                }
                is Edge -> {
                    res += toSvg(listOf(figure.realBeginPoint!!, figure.realEndPoint!!))
                }
            }
        }
        res += "</svg>"
        return res
    }

    private fun toSvg(circle: Circle): String {
        return "<circle cx=\"" + circle.center.x +
                "\" cy=\"" + circle.center.y + "\" r=\"" + circle.radius + "\" stroke=\"#000000\" stroke-width=\"1\" fill=\"#ffffff\"></circle>"
    }

    private fun getLines(pts: List<Point>): String {
        var res = ""
        for (pt in pts) {
            res += " L ${pt.x} ${pt.y}"
        }
        res += " L ${pts[0].x} ${pts[0].y}"
        return res
    }

    private fun toSvg(pts: List<Point>): String {
        return "<path d=\"M ${pts[0].x} ${pts[0].y}" + getLines(pts) + "\" stroke-width=\"1\" stroke=\"black\" fill=\"transparent\"/>"
    }
}