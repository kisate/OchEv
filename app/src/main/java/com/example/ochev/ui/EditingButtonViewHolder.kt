package com.example.ochev.ui

import android.widget.FrameLayout
import android.widget.TextView
import com.example.ochev.R

class EditingButtonViewHolder(
    val item: FrameLayout
) {
    val textView = item.findViewById<TextView>(R.id.editing_button_text)
}