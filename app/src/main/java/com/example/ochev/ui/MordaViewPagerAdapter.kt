package com.example.ochev.ui

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MordaViewPagerAdapter(
    val activity: FragmentActivity,
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return ApplicationComponent.viewersHolder.size()
    }

    fun getIndex(id: String): Int {
        return ApplicationComponent.viewersHolder.getIndex(id)
    }

    override fun createFragment(p0: Int): Fragment {
        val id = ApplicationComponent.viewersHolder.getId(p0)
            ?: return GraphFragment.newInstance(p0.toString())
        return GraphFragment.newInstance(id)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun createAndAddNewFragment() {
        val count = itemCount
        ApplicationComponent.viewersHolder.createAndAddNewViewer(activity)
        notifyItemInserted(count)
    }
}