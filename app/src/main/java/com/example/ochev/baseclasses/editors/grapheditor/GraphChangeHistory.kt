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
            graphEditor.graph = past.last()
            future.add(past.removeAt(past.lastIndex))
        }
    }

    fun undoRevert() {
        if (future.isNotEmpty()) {
            graphEditor.graph = future.last()
            past.add(future.removeAt(future.lastIndex))
        }
    }

    fun saveState() {
        future.clear()
        if (past.size >= 50) past.removeAt(0)
        past.add(graphEditor.graph)
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