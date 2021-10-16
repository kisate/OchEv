package com.example.ochev.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.ochev.R
import com.example.ochev.Utils.Provider
import com.example.ochev.Utils.toPx
import com.example.ochev.baseclasses.editors.boardeditor.BoardViewer

class HistoryButtonsController(
    private val settingsView: ConstraintLayout,
    private val viewerProvider: Provider<BoardViewer?>,
) {

    init {
        val backHolder = HistoryButtonViewHolder(getItem())
        backHolder.image.setImageResource(R.drawable.arrow_left)
        backHolder.item.elevation = (50).toPx
        backHolder.item.setOnClickListener {
            viewerProvider.get()?.undoChange()
        }

        val forwardHolder = HistoryButtonViewHolder(getItem())
        forwardHolder.image.setImageResource(R.drawable.arrow_right)
        forwardHolder.item.elevation = (50).toPx
        forwardHolder.item.setOnClickListener {
            viewerProvider.get()?.redoChange()
        }

        backHolder.item.id = R.id.back_button_id
        forwardHolder.item.id = R.id.forward_button_id

        val width = (60).toPx.toInt()
        val height = (40).toPx.toInt()


        backHolder.item.layoutParams = ViewGroup.LayoutParams(width, height)
        forwardHolder.item.layoutParams = ViewGroup.LayoutParams(width, height)

        settingsView.addView(backHolder.item)
        settingsView.addView(forwardHolder.item)


        val set = ConstraintSet()
        set.clone(settingsView)
        set.connect(
            R.id.back_button_id,
            ConstraintSet.BOTTOM,
            settingsView.id,
            ConstraintSet.BOTTOM,
            (16).toPx.toInt()
        )
        set.connect(
            R.id.back_button_id,
            ConstraintSet.LEFT,
            settingsView.id,
            ConstraintSet.LEFT,
            (16).toPx.toInt()
        )
        set.connect(
            R.id.forward_button_id,
            ConstraintSet.BOTTOM,
            settingsView.id,
            ConstraintSet.BOTTOM,
            (16).toPx.toInt()
        )
        set.connect(
            R.id.forward_button_id,
            ConstraintSet.LEFT,
            R.id.back_button_id,
            ConstraintSet.RIGHT,
            (32).toPx.toInt()
        )
        set.applyTo(settingsView)
    }

    fun show() {
        settingsView.visibility = View.VISIBLE
    }

    fun hide() {
        settingsView.visibility = View.GONE
    }

    private fun getItem(): FrameLayout {
        return LayoutInflater.from(settingsView.context)
            .inflate(R.layout.history_button, null) as FrameLayout
    }
}