package com.example.ochev

import android.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_main.*
import viewclasses.DrawStrokeInteractor
import viewclasses.DrawStrokeView
import viewclasses.StrokeInputView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val strokeInput = StrokeInputView(this, null, drawOutputId)
        strokeInput.alpha = 0F

        addContentView(strokeInput,
            RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
        )

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
