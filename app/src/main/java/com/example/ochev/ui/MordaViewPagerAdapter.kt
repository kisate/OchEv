package com.example.ochev.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor

class MordaViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(p0: Int): Fragment {
        return GraphFragment(GraphEditor())
    }
}