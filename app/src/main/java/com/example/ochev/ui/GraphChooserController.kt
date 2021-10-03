package com.example.ochev.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.ochev.R

class GraphChooserController(
    private val context: Context,
    private val changer: CurrentGraphChanger,
) {
    fun showPopup() {
        val holder = GraphChooserPopupViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.graph_chooser_popup, null) as ConstraintLayout
        )

        for (viewer in ApplicationComponent.viewersHolder.getViewers()) {
            val iconHolder = GraphIconViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.graph_icon, null) as FrameLayout
            )
            iconHolder.iconImage.setImageDrawable(context.resources.getDrawable(R.drawable.black_button))
            holder.graphIconsContainer.addView(iconHolder.item)
            iconHolder.item.setOnClickListener {
                changer.getViewToShowPopup().removeView(holder.item)
                holder.item.visibility = View.GONE
                changer.changeTo(viewer)
            }
        }
        changer.getViewToShowPopup().addView(holder.item)
    }
}