package com.example.ochev.ui

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.example.ochev.R

class GraphChooserController(
    private val context: Context,
    private val changer: CurrentGraphChanger,
    private val popupController: PopupController,
) {
    fun showPopup() {
        val holder = GraphChooserPopupViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.graph_chooser_popup, null) as LinearLayout
        )

        for (entry in ApplicationComponent.viewersHolder.entries()) {
            val iconHolder = GraphIconViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.graph_icon, null) as FrameLayout
            )
            iconHolder.iconImage.setImageDrawable(context.resources.getDrawable(R.drawable.white_button))
            holder.graphIconsContainer.addView(iconHolder.item)
            iconHolder.item.setOnClickListener {
                popupController.dismissPopup()
                changer.changeTo(entry.key)
            }
        }
        popupController.showPopup(Popup(holder.graphIconsContainer) {})
    }
}