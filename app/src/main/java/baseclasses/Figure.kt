package baseclasses


// Base object of our scheme

abstract class Figure(protected val in_text: MutableList<Char> = ArrayList()) {

    val text: List<Char>
        get() = in_text.toList()

    fun addSymbol(symbol_to_add: Char) {
        in_text.add(symbol_to_add)
    }

    fun removeLastSymbol() {
        if (!(in_text.isEmpty())) in_text.removeAt(in_text.lastIndex)
    }
}