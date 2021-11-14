package com.example.ochev.ui.graphchooser

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ochev.R
import com.example.ochev.Utils.Item
import com.example.ochev.Utils.RecyclerViewAdapterWithDelegates
import com.example.ochev.Utils.toPx
import com.example.ochev.ui.ApplicationComponent
import com.example.ochev.ui.Popup
import com.example.ochev.ui.PopupController

class GraphChooserController(
    private val context: Context,
    private val changer: CurrentGraphChanger,
    private val popupController: PopupController,
) {
    @SuppressLint("NotifyDataSetChanged")
    fun showPopup() {
        val holder = GraphChooserPopupViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.graph_chooser_popup, null) as FrameLayout
        )
        val items: ArrayList<Item> = arrayListOf()

        items.add(ChooserDelimItem())
        for (entry in ApplicationComponent.viewersHolder.entries()) {
            items.add(GraphChooserItem(entry.key))
            items.add(ChooserDelimItem())
        }
        val adapter = RecyclerViewAdapterWithDelegates(
            items,
            listOf(ChooserDelimDelegate(), GraphChooserDelegate(popupController, changer))
        )
        holder.recycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        holder.recycler.adapter = adapter
        adapter.notifyDataSetChanged()
        popupController.showPopup(Popup(holder.graphIconsContainer) {}, 0.15f, 0.85f, 0.2f, 0.8f)
    }
}