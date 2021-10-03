package com.example.ochev

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.ochev.ui.ApplicationComponent
import com.example.ochev.ui.CurrentGraphChangerImpl
import com.example.ochev.ui.GraphChooserController
import com.example.ochev.ui.MordaViewPagerAdapter


class RTVRedactorMainActivity : FragmentActivity() {
    private val mPager: ViewPager2
        get() {
            return findViewById(R.id.main_activity_pager)
        }
    private val container: RelativeLayout
        get() {
            return findViewById(R.id.main_activity_container)
        }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_acitvity_view)
        if (ApplicationComponent.viewersHolder.isEmpty()) {
            ApplicationComponent.viewersHolder.createAndAddNewViewer(this, "0")
        }
        val adapter = MordaViewPagerAdapter(this)
        ApplicationComponent.callbackToCreateNewBoard = Runnable {
            adapter.createAndAddNewFragment()
            mPager.setCurrentItem(adapter.itemCount - 1, true)
        }
        mPager.adapter = adapter
        adapter.notifyDataSetChanged()
        mPager.isUserInputEnabled = false

        val chooser = GraphChooserController(this, CurrentGraphChangerImpl(container, mPager))
        ApplicationComponent.callbackToShowPopup = Runnable {
            chooser.showPopup()
        }
    }

    override fun onDestroy() {
        ApplicationComponent.callbackToShowPopup = null
        ApplicationComponent.callbackToCreateNewBoard = null
        super.onDestroy()
    }
}
