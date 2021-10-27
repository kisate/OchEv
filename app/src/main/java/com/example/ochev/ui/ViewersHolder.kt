package com.example.ochev.ui

import android.content.Context
import com.example.ochev.baseclasses.cacheparser.CacheParser
import com.example.ochev.baseclasses.editors.boardeditor.BoardViewer
import com.example.ochev.baseclasses.editors.boardeditor.ViewerFactory
import com.example.ochev.ml.Classifier
import java.util.*
import kotlin.collections.HashMap

class ViewersHolder {
    private var count = 0

    private var currentMaxIndex = 0

    private val pendingViewerInfoList: Queue<PendingViewerInfo> = LinkedList()

    private var order: HashMap<String, Int> = LinkedHashMap()

    private val viewers: HashMap<String, BoardViewer> = LinkedHashMap()

    fun createAndAddNewViewer(context: Context, parser: CacheParser? = null) {
        val pendingViewer = if (parser == null) {
            ViewerFactory.create(Classifier(context))
        } else {
            ViewerFactory.create(Classifier(context), parser)
        }
        val id = count.toString()
        viewers[id] = pendingViewer
        pendingViewerInfoList.add(PendingViewerInfo(pendingViewer, id))
        count++
    }

    fun getViewerTagByIndex(index: Int): String {
        for (entry in order.entries) {
            if (entry.value == index) {
                return entry.key
            }
        }
        return "-1"
    }

    fun deleteViewer(id: String) {
        currentMaxIndex--
        val index = order[id] ?: return
        viewers.remove(id)
        pendingViewerInfoList.removeIf { it.id == id }
        order.remove(id)
        for (entry in order.entries) {
            if (entry.value > index) {
                order[entry.key] = entry.value - 1
            }
        }
    }

    fun onPendingViewerAttached() {
        val info = pendingViewerInfoList.peek() ?: return
        pendingViewerInfoList.remove()
        order[info.id] = currentMaxIndex
        currentMaxIndex++
    }

    fun pendingViewerInfo(): PendingViewerInfo? {
        return pendingViewerInfoList.peek()
    }

    fun getIndex(id: String): Int {
        return order[id] ?: -1
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

    fun invalidate() {
        currentMaxIndex = 0
        pendingViewerInfoList.clear()
        for (entry in viewers) {
            pendingViewerInfoList.add(PendingViewerInfo(entry.value, entry.key))
        }
    }
}