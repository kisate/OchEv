package baseclasses


// Base object of our scheme

abstract class Figure(val figureText: MutableList<Char> = ArrayList())


fun Figure.addSymbolToText(symbolToAdd: Char) {
    figureText.add(symbolToAdd)
}

fun Figure.removeLastSymbolFromText() {
    if (!figureText.isEmpty()) figureText.removeAt(figureText.lastIndex)
}

