package com.example.ochev.ui.graphchooser

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.ochev.Utils.Delegate
import com.example.ochev.Utils.Item
import com.example.ochev.R

class ChooserDelimDelegate: Delegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ChooserDelimViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.delim_chooser,  null
            ) as FrameLayout
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) = Unit

    override fun match(item: Item): Boolean {
        return item is ChooserDelimItem
    }
}