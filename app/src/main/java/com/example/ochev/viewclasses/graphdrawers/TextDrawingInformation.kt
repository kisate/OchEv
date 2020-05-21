package com.example.ochev.viewclasses.graphdrawers

import android.graphics.Paint
import android.graphics.Rect
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.vertexfigures.Circle
import com.example.ochev.baseclasses.vertexfigures.Rectangle
import com.example.ochev.baseclasses.vertexfigures.VertexFigure
import kotlin.math.*

class TextDrawingInformation(
    figure: VertexFigure,
    text: String,
    val paint: Paint
) {

    var coveringRect = Rect()
    var x = 0f
    var y = 0f

    init {
        coveringRect = calcRect(figure)
        paint.textSize = 0f
        if (!text.isEmpty()) {
            val rect = Rect()
            while (true) {
                paint.getTextBounds(text, 0, text.length, rect)
                if (abs(coveringRect.width()) < abs(rect.width()) || abs(coveringRect.height()) < abs(
                        rect.height()
                    )
                ) {
                    break
                }
                paint.textSize++
            }
            val textWidth = paint.measureText(text)
            x = figure.center.x - textWidth / 2
            y = figure.center.y + abs(rect.height()) / 2
        }
    }

    private fun calcRect(figure: VertexFigure): Rect {
        when (figure) {
            is Rectangle -> {
                return Rect(
                    min(figure.leftDownCorner.x, figure.rightDownCorner.x).toInt(),
                    max(figure.leftDownCorner.y, figure.leftUpCorner.y).toInt(),
                    max(figure.leftDownCorner.x, figure.rightDownCorner.x).toInt(),
                    min(figure.leftDownCorner.y, figure.leftUpCorner.y).toInt()
                )
            }
            is Circle -> {
                return Rect(
                    0,
                    (2 * sin(Math.PI / 180f * 45f) * figure.radius).toInt(),
                    (2 * cos(Math.PI / 180f * 45f) * figure.radius).toInt(),
                    0
                )

            }
            else -> {
                return Rect()
            }
        }
    }


}