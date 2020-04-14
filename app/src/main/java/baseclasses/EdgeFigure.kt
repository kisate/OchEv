package baseclasses

// Figure that connects information blocks

abstract class EdgeFigure(
    text: MutableList<Char> = ArrayList(),
    in_colour: Int = 0
) : Figure(text) {

    var colour: Int = in_colour
        protected set(value) {
            field = value
        }

    fun changeColour(new_colour: Int) {
        colour = new_colour
    }

}