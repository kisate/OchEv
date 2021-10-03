package com.example.ochev.ui

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.ochev.R
import com.example.ochev.Utils.Utils
import com.example.ochev.Utils.toPx

class SideEnvironmentSettingsController(
    private val settingsView: LinearLayout,
    private val settingsEnterView: FrameLayout,
) {
    fun initialize() {
        var holder = EnvironmentSideSettingViewHolder(getItem())
        holder.textView.text = "Создать граф"
        settingsView.addView(holder.item)
        holder.item.setOnClickListener {
            hideSettings(false)
            ApplicationComponent.callbackToCreateNewBoard?.run()
        }

        settingsView.addView(getDelim())

        holder = EnvironmentSideSettingViewHolder(getItem())
        holder.textView.text = "Выбрать граф"
        settingsView.addView(holder.item)
        holder.item.setOnClickListener {
            hideSettings(false)
            ApplicationComponent.callbackToShowPopup?.run()
        }

        settingsView.visibility = View.GONE

        settingsEnterView.setOnClickListener {
            showSettings(true)
        }
    }

    fun showSettings(animate: Boolean) {
        settingsView.visibility = View.VISIBLE
        settingsEnterView.visibility = View.GONE
    }

    fun hideSettings(animate: Boolean) {
        settingsView.visibility = View.GONE
        settingsEnterView.visibility = View.VISIBLE
    }

    private fun getItem(): ConstraintLayout {
        return LayoutInflater.from(settingsView.context)
            .inflate(R.layout.environment_setting_button, null) as ConstraintLayout
    }

    private fun getDelim(): FrameLayout {
        return LayoutInflater.from(settingsView.context)
            .inflate(R.layout.delim_settings, null) as FrameLayout
    }
}