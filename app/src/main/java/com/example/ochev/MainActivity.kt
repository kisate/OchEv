package com.example.ochev

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.graphdrawers.GraphDrawer
import kotlinx.android.synthetic.main.activity_main.*
import com.example.ochev.viewclasses.StrokeInputView
import com.example.ochev.viewclasses.buttonshandler.ButtonsContainer
import com.example.ochev.viewclasses.buttonshandler.ButtonsHandler
import com.example.ochev.viewclasses.strokedrawers.StrokeDrawer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Suppress("CAST_NEVER_SUCCEEDS")
class MainActivity : AppCompatActivity() {

    private val classifier = Classifier(this)
    private var strokeInput: StrokeInputView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        val strokeDrawer = StrokeDrawer(drawStrokeId)
        val graphDrawer =
            GraphDrawer(drawGraphId)
        val buttonsHandler = ButtonsHandler(ButtonsContainer(clearButtonId, deleteButtonId, undoButtonId, forwardButtonId),graphDrawer)

        if (strokeInput == null) {
            strokeInput = StrokeInputView(this, null, strokeDrawer, graphDrawer, buttonsHandler, classifier)
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

    object Executor {
        val executorService: ExecutorService = Executors.newCachedThreadPool()
    }
}
