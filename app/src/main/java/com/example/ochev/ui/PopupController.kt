package com.example.ochev.ui

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

    fun showPopup(popup: Popup) {
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