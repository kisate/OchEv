package com.example.ochev.viewclasses.graphdrawers

import android.graphics.Paint
import android.graphics.Rect
import com.example.ochev.baseclasses.vertexfigures.Rectangle
import kotlin.coroutines.coroutineContext
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class TextDrawingInformation(
    figure: Rectangle,
    text: String,
    val paint: Paint
) {

    var coveringRect = Rect()
    var x = 0f
    var y = 0f


    init {

        val xLeft = min(figure.leftDownCorner.x, figure.rightDownCorner.x).toInt()
        val xRight = max(figure.leftDownCorner.x, figure.rightDownCorner.x).toInt()
        val yTop = max(figure.leftDownCorner.y, figure.leftUpCorner.y).toInt()
        val yBot = min(figure.leftDownCorner.y, figure.leftUpCorner.y).toInt()

        coveringRect = Rect(xLeft, yTop, xRight, yBot)
        paint.textSize = 3f
        val rect = Rect()
        while (true){
            paint.getTextBounds(text, 0, text.length, rect)
            if (abs(coveringRect.width()) < abs(rect.width()) || abs(coveringRect.height()) < abs(rect.height())){
                break
            }
            paint.textSize++
        }
        val textWidth = paint.measureText(text)
        x = figure.center.x- textWidth/2
        y = figure.center.y + abs(rect.height())/2

    }


}