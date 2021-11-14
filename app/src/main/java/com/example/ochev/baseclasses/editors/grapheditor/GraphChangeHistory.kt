package com.example.ochev.baseclasses.editors.grapheditor

import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Vector

class GraphChangeHistory(
    val past: MutableList<Graph> = ArrayList(),
    val future: MutableList<Graph> = ArrayList(),
    val graphEditor: GraphEditor
) {
    fun isRevertible(): Boolean {
        return past.isNotEmpty()
    }

    fun isUndoRevertible(): Boolean {
        return future.isNotEmpty()
    }

    fun revert(): Graph {
        return if (isRevertible()) {
            if (future.isNotEmpty()) {
                future.add(graphEditor.graph.copy())
                val value = past.last()
                past.removeAt(past.lastIndex)
                value
            } else {
                future.add(graphEditor.graph.copy())
                val value = past.last()
                past.removeAt(past.lastIndex)
                value
            }
        } else graphEditor.graph
    }

    fun undoRevert(): Graph {
        return if (isUndoRevertible()) {
            past.add(graphEditor.graph.copy())
            val value = future.last()
            future.removeAt(future.lastIndex)
            value
        } else graphEditor.graph
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

    fun moveByVector(vector: Vector) {
        past.replaceAll {
            val editor = GraphEditor(it)
            editor.moveGraphByVector(vector)
            editor.graph
        }
        future.replaceAll {
            val editor = GraphEditor(it)
            editor.moveGraphByVector(vector)
            editor.graph
        }
    }
}