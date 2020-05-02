package com.example.ochev.baseclasses.vertexfigures

enum class Vertexes(val value: Int) {
    CIRCLE(0),
    RECTANGLE(1);
    //TRIANGLE(2);

    companion object {
        fun fromInt(value: Int) = values().firstOrNull { it.value == value }
    }
}