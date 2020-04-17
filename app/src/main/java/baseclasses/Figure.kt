package baseclasses

// Base object of our scheme

abstract class Figure(val figureText: MutableList<Char> = ArrayList())

class FigureInteractor {

    fun addSymbolToText(figure: Figure, symbolToAdd: Char) {
        figure.figureText.add(symbolToAdd)
    }

    fun removeLastSymbolFromText(figure: Figure) {
        if (!figure.figureText.isEmpty()) figure.figureText.removeAt(figure.figureText.lastIndex)
    }

}



