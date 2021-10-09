package com.example.ochev.ui

import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.ochev.R
import com.example.ochev.Utils.Provider
import com.example.ochev.Utils.toPx
import com.example.ochev.baseclasses.editors.boardeditor.BoardManipulator

class EditingButtonsController(
    private val settingsView: ConstraintLayout,
    private val viewerProvider: Provider<BoardManipulator?>
) {
    init {
        val deleteHolder = EditingButtonViewHolder(getItem())
        deleteHolder.textView.text = "Удалить"
        settingsView.addView(deleteHolder.item)
        deleteHolder.item.setOnClickListener {

        }

        val copyHolder = EditingButtonViewHolder(getItem())
        copyHolder.textView.text = "Копировать"
        settingsView.addView(copyHolder.item)
        copyHolder.item.setOnClickListener {

        }

        copyHolder.item.id = R.id.copy_button_id
        deleteHolder.item.id = R.id.delete_button_id


        val set = ConstraintSet()
        set.clone(settingsView)
        set.connect(
            R.id.copy_button_id,
            ConstraintSet.BOTTOM,
            settingsView.id,
            ConstraintSet.BOTTOM,
            (16).toPx.toInt()
        )
        set.connect(
            R.id.copy_button_id,
            ConstraintSet.RIGHT,
            settingsView.id,
            ConstraintSet.RIGHT,
            (16).toPx.toInt()
        )
        set.connect(
            R.id.delete_button_id,
            ConstraintSet.BOTTOM,
            R.id.copy_button_id,
            ConstraintSet.TOP,
            (12).toPx.toInt()
        )
        set.connect(
            R.id.delete_button_id,
            ConstraintSet.RIGHT,
            settingsView.id,
            ConstraintSet.RIGHT,
            (16).toPx.toInt()
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
            .inflate(R.layout.editing_button, null) as FrameLayout
    }
}