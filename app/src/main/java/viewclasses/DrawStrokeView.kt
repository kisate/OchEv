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
        canvas?.drawPath(path, paint)
    }
}

class DrawStrokeInteractor {

    fun set(drawStrokeView: DrawStrokeView, stroke: Stroke) {
        for (point in stroke.points) {
            drawStrokeView.path.addCircle(
                point.x.toFloat(),
                point.y.toFloat(),
                3f,
                Path.Direction.CCW
            )
        }
        drawStrokeView.invalidate()

    }

}