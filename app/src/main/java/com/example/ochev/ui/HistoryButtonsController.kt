package com.example.ochev.ui

import android.animation.Animator
import android.animation.ValueAnimator
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

    private var backHolder: HistoryButtonViewHolder
    private var forwardHolder: HistoryButtonViewHolder

    private val animators: HashMap<HistoryButtonViewHolder, Animator?> = HashMap()

    val height = (50).toPx

    init {
        backHolder = HistoryButtonViewHolder(getItem())
        backHolder.image.setImageResource(R.drawable.arrow_left)
        backHolder.item.elevation = (50).toPx
        backHolder.item.setOnClickListener {
            viewerProvider.get()?.undoChange()
        }

        forwardHolder = HistoryButtonViewHolder(getItem())
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

        backHolder.item.translationY = this.height
        backHolder.item.translationY = this.height
    }

    fun showForward(animate: Boolean) {
        show(animate, forwardHolder)
    }

    fun hideForward(animate: Boolean) {
        hide(animate, forwardHolder)
    }

    fun showUndo(animate: Boolean) {
        show(animate, backHolder)
    }

    fun hideUndo(animate: Boolean) {
        hide(animate, backHolder)
    }

    private fun show(animate: Boolean, holder: HistoryButtonViewHolder) {
        if (animate) {
            animate(holder.item.translationY, 0.0f, holder)
        } else {
            holder.item.translationY = 0.0f
            holder.item.visibility = View.VISIBLE
        }
    }

    private fun hide(animate: Boolean, holder: HistoryButtonViewHolder) {
        if (animate) {
            animate(holder.item.translationY, height, holder)
        } else {
            holder.item.translationY = height
            holder.item.visibility = View.GONE
        }
    }

    private fun animate(from: Float, to: Float, viewHolder: HistoryButtonViewHolder) {
        animators[viewHolder]?.cancel()
        animators[viewHolder] = null
        val animator = ValueAnimator.ofFloat(from, to)

        animator.duration = (kotlin.math.abs(from - to) / height * 300).toLong()

        animator.addUpdateListener {
            val value = it.animatedValue as Float
            viewHolder.item.translationY = value
            viewHolder.item.alpha = 1 - value / height
        }

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {
                if (to == 0.0f) {
                    viewHolder.item.visibility = View.VISIBLE
                }
            }

            override fun onAnimationEnd(p0: Animator?) {
                if (to == height) {
                    viewHolder.item.visibility = View.INVISIBLE
                }
            }

            override fun onAnimationCancel(p0: Animator?) = Unit

            override fun onAnimationRepeat(p0: Animator?) = Unit
        })

        animators[viewHolder] = animator

        animator.start()
    }

    private fun getItem(): FrameLayout {
        return LayoutInflater.from(settingsView.context)
            .inflate(R.layout.history_button, null) as FrameLayout
    }
}