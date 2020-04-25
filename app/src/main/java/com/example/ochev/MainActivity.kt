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

class MainActivity : AppCompatActivity() {

    private val classifier = Classifier(this)
    private lateinit var strokeInput: StrokeInputView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        strokeInput = StrokeInputView(this, null, drawStrokeId, drawGraphId)
        strokeInput.alpha = 0F

        val layoutParams =  RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )

        relativeId.addView(strokeInput, layoutParams)

        clearButtonId.setOnClickListener {
            strokeInput.clear()
        }


        classifier
            .initialize()
            .addOnFailureListener {e -> Log.e("MainActivity", "Error to setting up classifier", e)}
    }



}
