package com.example.ochev

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import viewclasses.DrawStrokeInteractor
import viewclasses.StrokeInputView

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val strokeInput = StrokeInputView(this, null, drawOutputId)
        strokeInput.alpha = 0F

        val layoutParams =  RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )

        relativeId.addView(strokeInput, layoutParams)

        loadButtonId.setOnClickListener {
            strokeInput.inputHandler.loadStrokes("config.txt")
            DrawStrokeInteractor().clear(drawOutputId)
        }

        clearButtonId.setOnClickListener {
            val id = strokeInput.inputHandler.strokes.lastIndex
            if (id > 0) {
                strokeInput.inputHandler.strokes.removeAt(id)
            }
            DrawStrokeInteractor().clear(drawOutputId)
        }

    }



}
