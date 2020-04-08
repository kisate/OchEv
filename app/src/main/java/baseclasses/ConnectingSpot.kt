package baseclasses

/*
edge figures can connect to vertex figures only in connecting spots
 */

class ConnectingSpot(
    val id: Int = 0,
    val figure: VertexFigure
) {
    val x: Int
        get() = figure.getSpotX(id)
    val y: Int
        get() = figure.getSpotY(id)
}