package com.example.ochev

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import viewclasses.DrawStrokeInteractor
import viewclasses.StrokeInputView
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
            strokeInput.inputHandler.loadStrokes("strokes.txt")
            strokeInput.inputHandler.clear()
            DrawStrokeInteractor().clear(drawOutputId)
        }

        clearButtonId.setOnClickListener {
            strokeInput.inputHandler.clear()
            DrawStrokeInteractor().clear(drawOutputId)
        }

        fileClearId.setOnClickListener{
            try {
                val outputStreamWriter = OutputStreamWriter(
                    this.openFileOutput(
                        "config.txt",
                        Context.MODE_PRIVATE
                    )
                )
                outputStreamWriter.write("")
                outputStreamWriter.close()
            } catch (e: IOException) {
                Log.e("Exception", "File write failed: " + e.toString())
            }
        }

    }



}
