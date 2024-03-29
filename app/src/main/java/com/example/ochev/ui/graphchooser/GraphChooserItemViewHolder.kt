package com.example.ochev.ui.graphchooser

import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.ochev.R

class GraphChooserItemViewHolder(
    val item: ConstraintLayout
) : RecyclerView.ViewHolder(item) {
    val img = item.findViewById<ImageView>(R.id.graph_chooser_item_pic)
}