package com.example.ochev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.storage.StorageManager
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import viewclasses.StrokeInput

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val strokeInput: StrokeInput = strokeInputId


        loadButtonId.setOnClickListener {
            strokeInput.loadStrokes("config.txt")
        }

        clearButtonId.setOnClickListener {
            val id = strokeInput.strokes.lastIndex
            if (id > 0) {
                strokeInput.strokes.removeAt(id)
            }
        }

    }


}
