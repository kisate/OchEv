package viewclasses

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import baseclasses.dataclasses.Point
import baseclasses.dataclasses.Stroke
import baseclasses.dataclasses.StrokeInteractor

class DrawStrokeView(
    context: Context?,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    val paint: Paint = Paint()

    var path = Path()

    init {
        paint.setStyle(Paint.Style.STROKE)
        paint.strokeWidth = 10f
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawColor(Color.WHITE)
        canvas?.drawPath(path, paint)
    }
}

class DrawStrokeInteractor {

    private var lastId = 0

    fun set(drawStrokeView: DrawStrokeView, stroke: Stroke) {
        for (id in lastId..stroke.points.size - 1) {
            drawStrokeView.path.lineTo(
                stroke.points[id].x.toFloat(),
                stroke.points[id].y.toFloat()
            )
        }

        drawStrokeView.invalidate()
        lastId = stroke.points.size
    }

    fun clear(drawStrokeView: DrawStrokeView) {
        drawStrokeView.path.reset()
        drawStrokeView.invalidate()
        lastId = 0
    }

}