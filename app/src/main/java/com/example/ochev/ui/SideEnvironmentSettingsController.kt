package com.example.ochev.ui

import android.animation.Animator
import android.animation.ValueAnimator
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
    private val tagProvider: Provider<String?>,
    private val saveBitmap: Runnable,
) {
    private var currentAnimator: Animator? = null

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
            saveBitmap.run()
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
        settingsView.alpha = 0.0f

        settingsEnterView.setOnClickListener {
            showSettings(true)
        }

        settingsEnterView.setImageResource(R.drawable.menu_icon)
    }

    fun showSettings(animate: Boolean) {
        if (animate) {
            animateSettings(settingsView.alpha, 1.0f)
        } else {
            settingsView.visibility = View.VISIBLE
            settingsView.alpha = 1.0f
            settingsEnterView.visibility = View.GONE
            settingsEnterView.alpha = 0.0f
        }
    }

    fun hideSettings(animate: Boolean) {
        if (animate) {
            animateSettings(settingsView.alpha, 0.0f)
        } else {
            settingsView.visibility = View.GONE
            settingsView.alpha = 0.0f
            settingsEnterView.visibility = View.VISIBLE
            settingsEnterView.alpha = 1.0f
        }

    }

    private fun animateSettings(from: Float, to: Float) {
        currentAnimator?.cancel()
        currentAnimator = null
        val animator = ValueAnimator.ofFloat(from, to)

        animator.duration = (kotlin.math.abs(from - to) * 150).toLong()

        animator.addUpdateListener {
            val value = it.animatedValue as Float
            settingsView.alpha = value
            settingsEnterView.alpha = 1 - value
        }

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {
                if (from == 0.0f) {
                    settingsView.visibility = View.VISIBLE
                }
                if (from == 1.0f) {
                    settingsEnterView.visibility = View.VISIBLE
                }
            }

            override fun onAnimationEnd(p0: Animator?) {
                if (to == 1.0f) {
                    settingsEnterView.visibility = View.GONE
                }
                if (to == 0.0f) {
                    settingsView.visibility = View.GONE
                }
            }

            override fun onAnimationCancel(p0: Animator?) = Unit

            override fun onAnimationRepeat(p0: Animator?) = Unit
        })

        currentAnimator = animator
        animator.start()
    }

    fun isShown(): Boolean {
        return settingsView.visibility == View.VISIBLE
    }

    fun onDestroy() {
        currentAnimator?.cancel()
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