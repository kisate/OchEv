package com.example.ochev.baseclasses.dataclasses.nodes

import com.example.ochev.baseclasses.editors.edgefigures.Edge

data class EdgeNode(
    override val id: Int = 0,
    override val figure: Edge
) : FigureNode()