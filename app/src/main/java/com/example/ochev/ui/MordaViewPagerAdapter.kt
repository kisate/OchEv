package com.example.ochev.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MordaViewPagerAdapter(
    private val activity: FragmentActivity,
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return ApplicationComponent.viewersHolder.size()
    }

    fun getIndex(id: String): Int {
        return ApplicationComponent.viewersHolder.getIndex(id)
    }

    override fun createFragment(p0: Int): Fragment {
        val id = ApplicationComponent.viewersHolder.pendingViewerInfo()?.id
        if (id == null) {
            ApplicationComponent.viewersHolder.createAndAddNewViewer(activity)
            return createFragment(p0)
        }
        val fragment = GraphFragment.newInstance(id)
        ApplicationComponent.viewersHolder.onPendingViewerAttached()
        return fragment
    }

    fun createAndAddNewFragment() {
        val count = itemCount
        ApplicationComponent.viewersHolder.createAndAddNewViewer(activity)
        notifyItemInserted(count)
    }
}