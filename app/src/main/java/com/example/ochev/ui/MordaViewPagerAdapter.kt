package com.example.ochev.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ochev.baseclasses.editors.boardeditor.BoardViewer

class MordaViewPagerAdapter(
    private val activity: FragmentActivity,
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return ApplicationComponent.viewersHolder.size()
    }

    override fun createFragment(p0: Int): Fragment {
        ApplicationComponent.viewersHolder.createAndAddNewViewer(activity, p0.toString())
        return GraphFragment.newInstance(p0.toString())
    }

    fun createAndAddNewFragment() {
        val count = itemCount
        ApplicationComponent.viewersHolder.createAndAddNewViewer(activity, count.toString())
        notifyItemInserted(count)
    }
}