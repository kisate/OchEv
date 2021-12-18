package com.example.ochev.ui

import android.view.View
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.TextView
import com.example.ochev.R

interface EditingButtonHolder {
    val item: View
    val length: Int
}

class EditingButtonViewHolder(
    override val item: FrameLayout,
    override val length: Int,
) : EditingButtonHolder {
    val textView = item.findViewById<TextView>(R.id.editing_button_text)
}

class EditingSeekBarViewHolder(
    override val item: SeekBar,
    override val length: Int,
) : EditingButtonHolder