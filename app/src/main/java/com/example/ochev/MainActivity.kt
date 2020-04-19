package com.example.ochev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import viewclasses.StrokeInputView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val strokeInput: StrokeInputView = strokeInputId


        loadButtonId.setOnClickListener {
            strokeInput.inputHandler.loadStrokes("config.txt")
        }

        clearButtonId.setOnClickListener {
            val id = strokeInput.inputHandler.strokes.lastIndex
            if (id > 0) {
                strokeInput.inputHandler.strokes.removeAt(id)
            }
        }

    }


}
