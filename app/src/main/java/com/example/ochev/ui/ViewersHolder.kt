package com.example.ochev.ui

import android.content.Context
import com.example.ochev.baseclasses.editors.boardeditor.BoardViewer
import com.example.ochev.baseclasses.editors.boardeditor.ViewerFactory
import java.util.*
import kotlin.collections.HashMap

class ViewersHolder {
    private var count = 0

    private val pendingViewerInfoList: Queue<PendingViewerInfo> = LinkedList()

    private var order: HashMap<String, Int> = LinkedHashMap()

    private val viewers: HashMap<String, BoardViewer> = HashMap()

    fun createAndAddNewViewer(context: Context) {
        val pendingViewer = ViewerFactory.create(context)
        val id = count.toString()
        viewers[id] = pendingViewer
        pendingViewerInfoList.add(PendingViewerInfo(pendingViewer, id))
        order[id] = count
        count++
    }

    fun onPendingViewerAttached() {
        pendingViewerInfoList.remove()
    }

    fun pendingViewerInfo(): PendingViewerInfo? {
        return pendingViewerInfoList.peek()
    }

    fun getIndex(id: String): Int {
        return order[id] ?: 0
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

    fun getViewers(): List<BoardViewer> {
        return viewers.values.toList()
    }

    fun invalidate() {
        pendingViewerInfoList.clear()
        for (entry in viewers) {
            pendingViewerInfoList.add(PendingViewerInfo(entry.value, entry.key))
        }
    }
}