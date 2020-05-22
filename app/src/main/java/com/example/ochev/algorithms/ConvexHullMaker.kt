package com.example.ochev.algorithms

import com.example.ochev.baseclasses.dataclasses.Stroke

class ConvexHullMaker {
    fun getConvexHull(strokes: MutableList<Stroke>): Stroke {
        return slowConvexHull(strokes)
    }
}