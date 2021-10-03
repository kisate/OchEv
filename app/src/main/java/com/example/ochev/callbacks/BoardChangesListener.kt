package com.example.ochev.callbacks

import com.example.ochev.baseclasses.dataclasses.nodes.FigureNode

fun interface BoardChangesListener {
    fun onBoardChanged(figures: List<FigureNode>)
}