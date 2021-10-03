package com.example.ochev.baseclasses.editors.boardeditor

import com.example.ochev.baseclasses.dataclasses.Point

interface BoardManipulator {
    fun putPoint(pt: Point): BoardManipulator?

    fun actionIsOver(): Boolean
}