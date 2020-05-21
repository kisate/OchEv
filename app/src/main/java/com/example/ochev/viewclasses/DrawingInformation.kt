package com.example.ochev.viewclasses

enum class DrawingMode(value: Int){
    DEFAULT(0),
    EDIT(1),
    EDIT_CORNERS(2);
}

class DrawingInformation {
    var drawingMode = DrawingMode.DEFAULT
    var text = "TEST"
}