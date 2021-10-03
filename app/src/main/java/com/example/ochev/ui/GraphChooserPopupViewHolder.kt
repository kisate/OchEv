package com.example.ochev.ui

import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.ochev.R

class GraphChooserPopupViewHolder(
    val item: ConstraintLayout
) {
    val graphIconsContainer: LinearLayout = item.findViewById(R.id.graph_icons_container)
}