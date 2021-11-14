package com.example.ochev.ui.graphchooser

import androidx.viewpager2.widget.ViewPager2
import com.example.ochev.ui.MordaViewPagerAdapter

class CurrentGraphChangerImpl(
    private val mPager: ViewPager2,
) : CurrentGraphChanger {
    override fun changeTo(id: String) {
        val adapter = mPager.adapter as MordaViewPagerAdapter
        mPager.setCurrentItem(adapter.getIndex(id), true)
    }
}