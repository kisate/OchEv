package com.example.ochev

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.ochev.baseclasses.dataclasses.LineSegment
import com.example.ochev.baseclasses.dataclasses.Point
import com.example.ochev.baseclasses.dataclasses.Point.Companion.getOptimalSegment
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.drawers.GraphDrawer
import kotlinx.android.synthetic.main.activity_main.*
import com.example.ochev.viewclasses.StrokeInputView
import com.example.ochev.viewclasses.buttonshandler.ButtonsContainer
import com.example.ochev.viewclasses.buttonshandler.ButtonsHandler
import com.example.ochev.viewclasses.drawers.LinesDrawer
import com.example.ochev.viewclasses.strokedrawers.StrokeDrawer
import com.google.android.gms.tasks.Task
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.HashMap

@Suppress("CAST_NEVER_SUCCEEDS")
class MainActivity : AppCompatActivity() {

    private val classifier = Classifier(this)
    private var strokeInput: StrokeInputView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        val strokeDrawer = StrokeDrawer(drawStrokeId)
        graphDrawer.graphView = drawGraphId
        linesDrawer.linesView = drawLinesId

        val buttonsHandler = ButtonsHandler(ButtonsContainer(clearButtonId, deleteButtonId, undoButtonId, forwardButtonId, saveButtonId, copyButtonId),graphDrawer)

        if (strokeInput == null) {
            strokeInput = StrokeInputView(this, null, strokeDrawer, graphDrawer, linesDrawer,buttonsHandler, findViewById(R.id.editText), classifier)
            strokeInput?.alpha = 0F
        }

        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )

        frameId.addView(strokeInput, layoutParams)


        classifier
            .initialize(Executor.executorService)
            .addOnFailureListener { e ->
                Log.e(
                    "MainActivity",
                    "Error to setting up classifier",
                    e
                )
            }
    }

    override fun onResume() {
        super.onResume()
        graphDrawer.invalidate()
    }

    object Executor {
        val executorService: ExecutorService = Executors.newCachedThreadPool()
    }

    companion object {
        val graphDrawer = GraphDrawer()
        val linesDrawer = LinesDrawer()
    }
}
