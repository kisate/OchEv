package com.example.ochev.ui

import android.widget.FrameLayout
import android.widget.ImageView
import com.example.ochev.R

class GraphIconViewHolder(
    val item: FrameLayout
) {
    val iconImage = item.findViewById<ImageView>(R.id.graph_icon_image)
}