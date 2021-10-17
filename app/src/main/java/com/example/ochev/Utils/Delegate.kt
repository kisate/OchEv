package com.example.ochev.Utils

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface Delegate {
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item)

    fun match(item: Item): Boolean
}