package com.example.ochev.ui

import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.ochev.R

class SideEnvironmentSettingsController(
    private val settingsView: LinearLayout,
    private val settingsEnterView: FrameLayout,
) {
    fun initialize() {
        var holder = EnvironmentSideSettingViewHolder(getItem())
        holder.textView.text = "Создать"
        settingsView.addView(holder.item)
        holder.item.setOnClickListener {
            ApplicationComponent.callbackToCreateNewBoard?.run()
        }

        settingsView.visibility = View.GONE

        settingsEnterView.setOnClickListener {
            showSettings()
        }
    }

    fun showSettings() {
        settingsView.visibility = View.VISIBLE
        settingsEnterView.visibility = View.GONE
    }

    fun hideSettings() {
        settingsView.visibility = View.GONE
        settingsEnterView.visibility = View.VISIBLE
    }

    private fun getItem(): ConstraintLayout {
        return LayoutInflater.from(settingsView.context)
            .inflate(R.layout.environment_setting_button, null) as ConstraintLayout
    }
}