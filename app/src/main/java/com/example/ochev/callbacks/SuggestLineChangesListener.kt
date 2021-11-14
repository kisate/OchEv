package com.example.ochev.callbacks

import com.example.ochev.baseclasses.dataclasses.LineSegment

fun interface SuggestLineChangesListener {
    fun onSuggestLineChanged(segment: List<LineSegment>)
}