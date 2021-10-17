package com.example.ochev.ui

import android.content.Context
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

    private val viewers: HashMap<String, BoardViewer> = HashMap()

    fun createAndAddNewViewer(context: Context) {
        val pendingViewer = ViewerFactory.create(Classifier(context))
        val id = count.toString()
        viewers[id] = pendingViewer
        pendingViewerInfoList.add(PendingViewerInfo(pendingViewer, id))
        order[id] = currentMaxIndex
        currentMaxIndex++
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
        pendingViewerInfoList.remove()
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