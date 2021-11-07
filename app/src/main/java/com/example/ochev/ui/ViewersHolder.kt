package com.example.ochev.ui

import android.content.Context
import com.example.ochev.baseclasses.cacheparser.CacheParser
import com.example.ochev.baseclasses.editors.boardeditor.BoardViewer
import com.example.ochev.baseclasses.editors.boardeditor.ViewerFactory
import com.example.ochev.ml.Classifier
import java.util.*

class ViewersHolder {
    private var count = 0

    private val viewers: HashMap<String, BoardViewer> = LinkedHashMap()

    fun createAndAddNewViewer(context: Context, parser: CacheParser? = null) {
        val pendingViewer = if (parser == null) {
            ViewerFactory.create(Classifier(context))
        } else {
            ViewerFactory.create(Classifier(context), parser)
        }
        val id = count.toString()
        viewers[id] = pendingViewer
        count++
    }

    fun deleteViewer(id: String): Int? {
        var indx = 0
        for (viewer in viewers.entries) {
            if (viewer.key == id) {
                viewers.remove(id)
                return indx
            }
            indx++
        }
        viewers.remove(id)
        return null
    }

    fun getId(index: Int): String? {
        var indx = 0
        for (viewer in viewers.entries) {
            if (indx == index) {
                return viewer.key
            }
        }
        return null
    }


    fun getIndex(id: String): Int {
        var indx = 0
        for (viewer in viewers.entries) {
            if (viewer.key == id) {
                return indx
            }
            indx++
        }
        return -1
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

    fun entries(): Set<Map.Entry<String, BoardViewer>> {
        return viewers.entries
    }
}