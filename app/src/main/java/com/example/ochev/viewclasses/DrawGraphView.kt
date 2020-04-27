package com.example.ochev.viewclasses

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.ochev.baseclasses.EdgeFigure
import com.example.ochev.baseclasses.Figure
import com.example.ochev.baseclasses.VertexFigure
import com.example.ochev.baseclasses.dataclasses.Graph
import com.example.ochev.baseclasses.edgefigures.Line
import com.example.ochev.baseclasses.vertexfigures.Circle
import com.example.ochev.baseclasses.vertexfigures.Rectangle
import com.example.ochev.baseclasses.vertexfigures.Triangle

class DrawGraphView(
    context: Context?,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    val graph = Graph()
    val drawGraphInteractor = DrawGraphInteractor()

    fun clear(){
        graph.vertexes.clear()
        graph.edges.clear()
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        for (vertex in graph.vertexes) {
            drawGraphInteractor.draw(vertex, canvas)
        }
        for (edge in graph.edges) {
            drawGraphInteractor.draw(edge, canvas)
        }
    }
}

class CircleDrawer {
    private val paint = Paint()

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f
        paint.color = Color.BLACK

    }

    fun draw(vertex: Circle, canvas: Canvas?) {
        canvas?.drawCircle(
            vertex.center.x.toFloat(),
            vertex.center.y.toFloat(),
            vertex.radius.toFloat(),
            paint
        )
    }
}

class RectangleDrawer {
    private val paint = Paint()

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f
        paint.color = Color.BLUE
    }

    fun draw(vertex: Rectangle, canvas: Canvas?) {
        canvas?.drawRect(vertex.toRect(), paint)
    }
}

class TriangleDrawer {
    private val paint = Paint()

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f
        paint.color = Color.RED
    }

    fun draw(vertex: Triangle, canvas: Canvas?) {
        val path = Path()
        path.moveTo(vertex.pointA.x.toFloat(), vertex.pointA.y.toFloat())
        path.lineTo(vertex.pointB.x.toFloat(), vertex.pointB.y.toFloat())
        path.lineTo(vertex.pointC.x.toFloat(), vertex.pointC.y.toFloat())
        path.lineTo(vertex.pointA.x.toFloat(), vertex.pointA.y.toFloat())
        path.lineTo(vertex.pointB.x.toFloat(), vertex.pointB.y.toFloat())
        canvas?.drawPath(path, paint)
    }
}

class LineDrawer {
    private val paint = Paint()

    init {
        paint.style = Paint.Style.STROKE
        paint.color = Color.DKGRAY
        paint.strokeWidth = 10f
    }

    fun draw(edge: Line, canvas: Canvas?) {
        val path = Path()
        val from = edge.beginFigure.center
        val to = edge.endFigure.center
        path.moveTo(from.x.toFloat(), from.y.toFloat())
        path.lineTo(to.x.toFloat(), to.y.toFloat())
        canvas?.drawPath(path, paint)
    }
}

class DrawGraphInteractor {
    private val circleDrawer = CircleDrawer()
    private val rectangleDrawer = RectangleDrawer()
    private val triangleDrawer = TriangleDrawer()
    private val lineDrawer = LineDrawer()

    fun draw(vertex: VertexFigure, canvas: Canvas?) {
        when (vertex) {
            is Circle -> circleDrawer.draw(vertex, canvas)
            is Rectangle -> rectangleDrawer.draw(vertex, canvas)
            is Triangle -> triangleDrawer.draw(vertex, canvas)
        }
    }

    fun draw(edge: EdgeFigure, canvas: Canvas?) {
        when (edge) {
            is Line -> lineDrawer.draw(edge, canvas)
        }
    }


}

