package com.example.ochev.ui

import android.view.View

data class Popup(
    val popupView: View,
    val dissmisCallback: Runnable
)