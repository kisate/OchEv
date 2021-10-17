package com.example.ochev.ui

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup

class PopupController(
    private val popupWindow: View,
    private val popupContainer: ViewGroup,
) {
    private var currentPopup: Popup? = null

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
        popupWindow.visibility = View.VISIBLE
        popupWindow.isClickable = true
        popupWindow.isFocusable = true
        popupContainer.addView(popup.popupView)
        currentPopup = popup

        popupWindow.setOnClickListener {
            popup.dissmisCallback.run()
            dismissPopup()
        }
    }

    fun dismissPopup() {
        currentPopup?.let {
            popupWindow.setOnClickListener { }
            popupContainer.removeView(it.popupView)
        }
        popupWindow.visibility = View.GONE
    }
}