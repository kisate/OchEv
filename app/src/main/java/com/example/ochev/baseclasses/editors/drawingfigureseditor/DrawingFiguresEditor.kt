package com.example.ochev.baseclasses.editors.drawingfigureseditor

import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.dataclasses.DrawingFigure
import com.example.ochev.viewclasses.DrawingInformation
import com.example.ochev.viewclasses.DrawingMode

class DrawingFiguresEditor {
    val figures: MutableList<DrawingFigure> = ArrayList()

    fun deleteFigure(figure: Figure) {
        for (index in 0 until figures.size) {
            if (figures[index].figure == figure) {
                figures.removeAt(index)
                break
            }
        }
    }

    fun addFigure(figure: Figure) {
        figures.add(DrawingFigure(figure, DrawingInformation()))
    }

    fun changeFigure(oldFigure: Figure, newFigure: Figure) {
        for (index in 0 until figures.size) {
            if (figures[index].figure == oldFigure) {
                figures[index] = DrawingFigure(newFigure, figures[index].drawingInformation)
            }
        }
    }

    fun changeFiguresDrawingInformation(figure: Figure, drawingMode: DrawingMode) {
        for (index in 0 until figures.size) {
            if (figures[index].figure == figure) {
                val drawingInformation = DrawingInformation()
                drawingInformation.drawingMode = drawingMode
                figures[index] = DrawingFigure(figure, drawingInformation)
            }
        }
    }

    fun clear() {
        figures.clear()
    }

    fun getDrawingInformation(figure: Figure): DrawingInformation {
        return figures.find{it.figure == figure}!!.drawingInformation
    }


}