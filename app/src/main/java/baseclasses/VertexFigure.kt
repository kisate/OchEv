package baseclasses

/*
A figure, that represents an information block in our scheme
 */

abstract class VertexFigure(
    text: MutableList<Char> = MutableList(0) { ' ' }
    , _texture_path: String = ""
) : Figure(text) {

    var texture_path: String = _texture_path
        protected set(value) {
            field = value
        }

    fun changeTexture(new_path: String) {
        texture_path = new_path
    }

    abstract fun changeSize(factor: Double)
    abstract fun moveByVector(direction: Vector)
}