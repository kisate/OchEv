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
import com.example.ochev.callbacks.UserMode

class EditingButtonsController(
    private val settingsView: ConstraintLayout,
    private val viewerProvider: Provider<BoardManipulator?>
) {
    private val deleteHolder: EditingButtonViewHolder
    private val copyHolder: EditingButtonViewHolder

    init {
        deleteHolder = EditingButtonViewHolder(getItem())
        deleteHolder.textView.text = "Удалить"
        deleteHolder.item.setOnClickListener {
            viewerProvider.get()?.deleteSelected()
        }

        copyHolder = EditingButtonViewHolder(getItem())
        copyHolder.textView.text = "Копировать"
        copyHolder.item.setOnClickListener {
            viewerProvider.get()?.copySelected()
        }

        copyHolder.item.id = R.id.copy_button_id
        deleteHolder.item.id = R.id.delete_button_id

        copyHolder.item.elevation = (50).toPx
        deleteHolder.item.elevation = (50).toPx
    }

    fun show(userMode: UserMode) {
        settingsView.visibility = View.VISIBLE
        when (userMode) {
            UserMode.EDITING__COPY_DISABLED -> {
                processHolder(listOf(deleteHolder))
            }

            UserMode.EDITING__COPY_ENABLED -> {
                processHolder(listOf(deleteHolder, copyHolder))
            }
        }
    }

    private fun processHolder(holders: List<EditingButtonViewHolder>) {
        settingsView.removeAllViews()
        holders.forEach { settingsView.addView(it.item) }
        val set = ConstraintSet()
        set.clone(settingsView)
        for (i in holders.indices) {
            if (i == 0) {
                set.connect(
                    holders[i].item.id,
                    ConstraintSet.BOTTOM,
                    settingsView.id,
                    ConstraintSet.BOTTOM,
                    (16).toPx.toInt()
                )
            } else {
                set.connect(
                    holders[i].item.id,
                    ConstraintSet.BOTTOM,
                    holders[i - 1].item.id,
                    ConstraintSet.TOP,
                    (12).toPx.toInt()
                )
            }
            set.connect(
                holders[i].item.id,
                ConstraintSet.RIGHT,
                settingsView.id,
                ConstraintSet.RIGHT,
                (16).toPx.toInt()
            )
        }

        set.applyTo(settingsView)
    }

    fun hide() {
        settingsView.visibility = View.GONE
    }

    private fun getItem(): FrameLayout {
        return LayoutInflater.from(settingsView.context)
            .inflate(R.layout.editing_button, null) as FrameLayout
    }
}