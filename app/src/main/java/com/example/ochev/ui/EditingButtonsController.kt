package com.example.ochev.ui

import android.animation.Animator
import android.animation.ValueAnimator
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

    private var currentAnimator: Animator? = null

    private val length = (160).toPx.toFloat()

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

    fun show(userMode: UserMode, animate: Boolean) {
        when (userMode) {
            UserMode.EDITING__COPY_DISABLED -> {
                processHolder(listOf(deleteHolder))
            }

            UserMode.EDITING__COPY_ENABLED -> {
                processHolder(listOf(deleteHolder, copyHolder))
            }
        }
        if (animate) {
            animate(settingsView.translationX, 0.0f)
        } else {
            settingsView.translationX = 0.0f
            settingsView.visibility = View.VISIBLE
        }

    }

    fun hide(animate: Boolean) {
        if (animate) {
            animate(settingsView.translationX, length)
        } else {
            settingsView.visibility = View.GONE
            settingsView.translationX = length
        }

    }

    fun onDestroy() {
        currentAnimator?.cancel()
    }

    private fun animate(from: Float, to: Float) {
        currentAnimator?.cancel()
        currentAnimator = null
        val animator = ValueAnimator.ofFloat(from, to)

        animator.duration = (kotlin.math.abs(from - to) / length * 300).toLong()

        animator.addUpdateListener {
            val value = it.animatedValue as Float
            settingsView.translationX = value
            settingsView.alpha = 1 - value / length
        }

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {
                if (to == 0.0f) {
                    settingsView.visibility = View.VISIBLE
                }
            }

            override fun onAnimationEnd(p0: Animator?) {
                if (to == length) {
                    settingsView.visibility = View.INVISIBLE
                }
            }

            override fun onAnimationCancel(p0: Animator?) = Unit

            override fun onAnimationRepeat(p0: Animator?) = Unit
        })

        currentAnimator = animator

        animator.start()
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

    private fun getItem(): FrameLayout {
        return LayoutInflater.from(settingsView.context)
            .inflate(R.layout.editing_button, settingsView, false) as FrameLayout
    }
}