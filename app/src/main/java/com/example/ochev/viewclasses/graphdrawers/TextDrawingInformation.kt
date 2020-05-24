package com.example.ochev.viewclasses.graphdrawers

import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Circle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Rectangle
import com.example.ochev.baseclasses.dataclasses.vertexfigures.VertexFigure
import kotlin.math.*

class TextDrawingInformation(
    figure: VertexFigure,
    text: String,
    val paint: Paint
) {

    private var coveringRect = Rect()
    var x = 0f
    var y = 0f

    init {
        coveringRect = calcRect(figure)
        paint.textSize = 50f
        if (text.isNotEmpty()) {
            x = coveringRect.left.toFloat()
            y = coveringRect.bottom.toFloat()
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

    private fun buildText(paint: Paint, words: List<String>, width: Int): List<String>? {
        val res = mutableListOf<String>()
        var currentLine = ""
        var added = false
        for (word in words) {
            if (paint.measureText("$currentLine $word") < width)
            {
                currentLine += word
                currentLine += " "
                added = true
            }
            else
            {
                res.add(currentLine)
                currentLine = ""
            }
        }
        res.add(currentLine)

        if (!added) return null

        return  res
    }

}