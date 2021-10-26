package com.example.ochev.ui

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup

class PopupController(
    private val popupWindow: View,
    private val popupContainer: ViewGroup,
) {
    private var currentPopup: Popup? = null
    private var currentAnimator: Animator? = null

    init {
        popupWindow.isClickable = true
        popupWindow.isFocusable = true
    }

    fun showPopup(popup: Popup, l: Float, r: Float, t: Float, d: Float) {
        val width = Resources.getSystem().displayMetrics.widthPixels
        val height = Resources.getSystem().displayMetrics.heightPixels
        popup.popupView.layoutParams =
            ViewGroup.LayoutParams((width * (r - l)).toInt(), (height * (d - t)).toInt())
        popup.popupView.isClickable = true
        dismissPopup()

        popup.popupView.alpha = 0.0f
        popupWindow.alpha = 0.0f
        val animator = ValueAnimator.ofFloat(0.0f, 1.0f)
        animator.addUpdateListener {
            val value = it.animatedValue as Float
            popup.popupView.alpha = value
            popupWindow.alpha = value
        }
        currentAnimator = animator

        popupWindow.visibility = View.VISIBLE
        popupWindow.isClickable = true
        popupWindow.isFocusable = true
        popupContainer.addView(popup.popupView)
        currentPopup = popup
        animator.start()

        popupWindow.setOnClickListener {
            popup.dissmisCallback.run()
            dismissPopup()
        }
    }

    fun endAnim() {
        currentAnimator?.end()
    }

    fun dismissPopup() {
        currentAnimator?.cancel()
        currentAnimator = null

        currentPopup?.let {
            popupWindow.setOnClickListener { }
            popupContainer.removeView(it.popupView)
        }
        popupWindow.visibility = View.GONE
    }
}