package com.example.ochev.algorithms.convexhull

import com.example.ochev.baseclasses.dataclasses.Stroke

class ConvexHullMaker {
    fun getConvexHull(strokes: MutableList<Stroke>): Stroke {
        return slowConvexHull(strokes)
    }
}