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

    val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    var path = Path()

    override fun onDraw(canvas: Canvas?) {
        if (path.isEmpty) {
            canvas?.drawColor(Color.WHITE)
        }
        canvas?.drawPath(path, paint)
    }
}

class DrawStrokeInteractor {

    private var lastId = 0

    fun set(drawStrokeView: DrawStrokeView, stroke: Stroke) {
        for (id in lastId..stroke.points.size - 1) {
            drawStrokeView.path.addCircle(
                stroke.points[id].x.toFloat(),
                stroke.points[id].y.toFloat(),
                1f,
                Path.Direction.CCW
            )
        }
        drawStrokeView.invalidate()
        lastId = stroke.points.size
    }

    fun clear(drawStrokeView: DrawStrokeView) {
        drawStrokeView.path = Path()
        drawStrokeView.invalidate()
        lastId = 0
    }

}