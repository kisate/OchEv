package com.example.ochev.ui

import android.content.Context
import com.example.ochev.baseclasses.editors.boardeditor.BoardViewer
import com.example.ochev.baseclasses.editors.boardeditor.ViewerFactory

class ViewersHolder {
    private val viewers: HashMap<String, BoardViewer> = HashMap()

    fun createAndAddNewViewer(context: Context, id: String) {
        viewers[id] = ViewerFactory.create(context)
    }

    fun size(): Int {
        return viewers.size
    }

    fun isEmpty(): Boolean {
        return size() == 0
    }

    fun getViewer(id: String): BoardViewer? {
        return viewers[id]
    }

    fun getViewers(): List<BoardViewer> {
        return viewers.values.toList()
    }
}