package com.example.ochev

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.DrawingMode
import com.example.ochev.viewclasses.GraphDrawer
import com.example.ochev.viewclasses.StrokeDrawer
import kotlinx.android.synthetic.main.activity_main.*
import com.example.ochev.viewclasses.StrokeInputView
import com.example.ochev.viewclasses.buttonshandler.ButtonsHandler
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val classifier = Classifier(this)
    private var strokeInput: StrokeInputView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        val strokeDrawer = StrokeDrawer(drawStrokeId)
        val graphDrawer = GraphDrawer(drawGraphId)
        val buttonsHandler = ButtonsHandler(clearButtonId, deleteButtonId, graphDrawer)

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
