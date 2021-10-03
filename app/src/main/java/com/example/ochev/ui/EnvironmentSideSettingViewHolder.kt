package com.example.ochev.ui

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.ochev.R

class EnvironmentSideSettingViewHolder(val item: ConstraintLayout) {
    val textView = item.findViewById<TextView>(R.id.environment_settings_button_text)
}