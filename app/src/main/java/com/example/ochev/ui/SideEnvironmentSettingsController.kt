package com.example.ochev.ui

import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.ochev.R
import com.example.ochev.Utils.Provider
import com.example.ochev.baseclasses.editors.boardeditor.BoardViewer

class SideEnvironmentSettingsController(
    private val settingsView: LinearLayout,
    private val settingsEnterView: ImageView,
    private val viewerProvider: Provider<BoardViewer?>,
    private val tagProvider: Provider<String?>
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
            ApplicationComponent.callbackToShowChooserPopup?.run()
        }

        settingsView.addView(getDelim())

        holder = EnvironmentSideSettingViewHolder(getItem())
        holder.textView.text = "Очистить граф"
        settingsView.addView(holder.item)
        holder.item.setOnClickListener {
            hideSettings(true)
            viewerProvider.get()?.clearBoard()
        }

        settingsView.addView(getDelim())

        holder = EnvironmentSideSettingViewHolder(getItem())
        holder.textView.text = "Удалить граф"
        settingsView.addView(holder.item)
        holder.item.setOnClickListener {
            hideSettings(false)
            val tag = tagProvider.get() ?: return@setOnClickListener
            ApplicationComponent.callbackToDeleteFragment?.close(tag)
        }

        settingsView.visibility = View.GONE

        settingsEnterView.setOnClickListener {
            showSettings(true)
        }

        settingsEnterView.setImageResource(R.drawable.menu_icon)
    }

    fun showSettings(animate: Boolean) {
        settingsView.visibility = View.VISIBLE
        settingsEnterView.visibility = View.GONE
    }

    fun hideSettings(animate: Boolean) {
        settingsView.visibility = View.GONE
        settingsEnterView.visibility = View.VISIBLE
    }

    fun isShown(): Boolean {
        return settingsView.visibility == View.VISIBLE
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