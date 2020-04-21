package com.example.ochev

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import com.example.ochev.viewclasses.DrawStrokeInteractor
import com.example.ochev.viewclasses.StrokeInputView
import java.io.IOException
import java.io.OutputStreamWriter

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
            strokeInput.inputHandler.saveStrokes("strokes.txt")
            strokeInput.inputHandler.clear()
            DrawStrokeInteractor().clear(drawOutputId)
        }

        clearButtonId.setOnClickListener {
            strokeInput.inputHandler.clear()
            DrawStrokeInteractor().clear(drawOutputId)
        }

    }



}
