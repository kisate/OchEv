package com.example.ochev.viewclasses

enum class DrawingMode(value: Int) {
    EDITING(1),
    DEFAULT(2);
}

class DrawingInformation {
    var drawingMode = DrawingMode.DEFAULT
}