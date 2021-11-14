package com.example.ochev.ui.graphchooser

import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.ochev.R

class GraphChooserPopupViewHolder(
    val graphIconsContainer: FrameLayout
) {
    val recycler = graphIconsContainer.findViewById<RecyclerView>(R.id.graphs_viewer)
}