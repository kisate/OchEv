package com.example.ochev.viewclasses

import com.example.ochev.viewclasses.graphdrawers.FigureStyle

enum class DrawingMode(value: Int){
    DEFAULT(0),
    EDIT(1),
    EDIT_CORNERS(2);
}

class DrawingInformation {
    var drawingMode = DrawingMode.DEFAULT
    var text = "TEST"
    var currentStyle = DrawingMode.DEFAULT.ordinal

    fun enterMode(newDrawingMode: DrawingMode) {
        drawingMode = newDrawingMode
        currentStyle = newDrawingMode.ordinal
    }

}