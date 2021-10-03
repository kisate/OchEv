package com.example.ochev.ui

import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.ochev.baseclasses.editors.boardeditor.BoardViewer

class CurrentGraphChangerImpl(
    private val mainView: RelativeLayout,
    private val mPager: ViewPager2,
) : CurrentGraphChanger {
    override fun changeTo(viewer: BoardViewer) {
        mPager.setCurrentItem(0, true)
    }

    override fun getViewToShowPopup(): ViewGroup {
        return mainView
    }
}