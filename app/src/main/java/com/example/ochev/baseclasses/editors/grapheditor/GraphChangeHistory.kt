package com.example.ochev.baseclasses.editors.grapheditor

import com.example.ochev.baseclasses.dataclasses.Graph
import com.example.ochev.baseclasses.dataclasses.Point

class GraphChangeHistory(
    val past: MutableList<Graph> = ArrayList(),
    val future: MutableList<Graph> = ArrayList(),
    val graphEditor: GraphEditor
) {
    fun revert() {
        if (past.isNotEmpty()) {
            if (future.isNotEmpty()) {
                future.add(graphEditor.graph.copy())
                graphEditor.graph = past.last()
                past.removeAt(past.lastIndex)
            } else {
                future.add(graphEditor.graph.copy())
                graphEditor.graph = past.last()
                past.removeAt(past.lastIndex)
            }
        }
    }

    fun undoRevert() {
        if (future.isNotEmpty()) {
            past.add(graphEditor.graph.copy())
            graphEditor.graph = future.last()
            future.removeAt(future.lastIndex)
        }
    }

    fun saveState() {
        future.clear()
        if (past.size >= 50) past.removeAt(0)
        past.add(graphEditor.graph.copy())
    }

    fun zoomByPointAndFactor(point: Point, factor: Float) {
        past.replaceAll {
            val editor = GraphEditor(it)
            editor.zoomByPointAndFactor(point, factor)
            editor.graph
        }
        future.replaceAll {
            val editor = GraphEditor(it)
            editor.zoomByPointAndFactor(point, factor)
            editor.graph
        }
    }
}