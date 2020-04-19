package viewclasses

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class DrawOutputView(
    context: Context?,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            canvas.save()
        }

    }
}