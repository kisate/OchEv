package com.example.ochev.ui.graphchooser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.ochev.Utils.Delegate
import com.example.ochev.Utils.Item
import com.example.ochev.R
import com.example.ochev.ui.PopupController

class GraphChooserDelegate(
    private val popupController: PopupController,
    private val currentGraphChanger: CurrentGraphChanger,
) : Delegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return GraphChooserItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.graph_chooser_item,  null
            ) as ConstraintLayout
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
        val itemHolder = holder as GraphChooserItemViewHolder
        val data = item as GraphChooserItem

        itemHolder.item.setOnClickListener {
            popupController.dismissPopup()
            currentGraphChanger.changeTo(data.id)
        }
        itemHolder.text.text = data.id
    }

    override fun match(item: Item): Boolean {
        return item is GraphChooserItem
    }
}