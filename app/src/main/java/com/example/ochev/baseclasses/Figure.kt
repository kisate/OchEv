package com.example.ochev.baseclasses

// Base object of our scheme

abstract class Figure(val figureText: MutableList<Char> = ArrayList()) {
    fun addSymbolTotext(symbolToAdd: Char) {
        figureText.add(symbolToAdd)
    }

    fun removeLastSymbolFromText() {
        if (!figureText.isEmpty()) figureText.removeAt(figureText.lastIndex)
    }
}




