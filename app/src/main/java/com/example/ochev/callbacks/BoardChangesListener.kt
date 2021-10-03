package com.example.ochev.callbacks

import com.example.ochev.baseclasses.dataclasses.Figure

fun interface BoardChangesListener {
    fun onBoardChanged(fingers: List<Figure>)
}