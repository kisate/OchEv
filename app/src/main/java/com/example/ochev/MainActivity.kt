package com.example.ochev

import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.ochev.baseclasses.dataclasses.StrokeInteractor
import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.InputHandler
import kotlinx.android.synthetic.main.activity_main.*
import com.example.ochev.viewclasses.StrokeInputView
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val classifier = Classifier(this)
    private var strokeInput: StrokeInputView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        if (strokeInput == null) {
            strokeInput = StrokeInputView(this, null, drawStrokeId, drawGraphId, classifier)
            strokeInput?.alpha = 0F
        }

        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )

        relativeId.addView(strokeInput, layoutParams)

        clearButtonId.setOnClickListener {
            strokeInput?.clear()
        }

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
