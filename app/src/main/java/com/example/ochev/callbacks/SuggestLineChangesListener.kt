package com.example.ochev.callbacks

import com.example.ochev.baseclasses.dataclasses.LineSegment

interface SuggestLineChangesListener {
    fun onSuggestLineChanged(segment: List<LineSegment>)


}