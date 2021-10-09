package com.example.ochev

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.ochev.ui.*


class RTVRedactorMainActivity : FragmentActivity() {
    private val mPager: ViewPager2
        get() {
            return findViewById(R.id.main_activity_pager)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_acitvity_view)

        initPager()
        initPopups()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initPager() {
        if (ApplicationComponent.viewersHolder.isEmpty()) {
            ApplicationComponent.viewersHolder.createAndAddNewViewer(this)
        } else {
            ApplicationComponent.viewersHolder.invalidate()
        }
        val adapter = MordaViewPagerAdapter(this)
        ApplicationComponent.callbackToCreateNewBoard = Runnable {
            adapter.createAndAddNewFragment()
            mPager.setCurrentItem(adapter.itemCount - 1, true)
        }
        mPager.adapter = adapter
        adapter.notifyDataSetChanged()
        mPager.isUserInputEnabled = false
    }

    private fun initPopups() {
        val popupController =
            PopupController(findViewById(R.id.popup_window), findViewById(R.id.popup_container))
        val chooser = GraphChooserController(this, CurrentGraphChangerImpl(mPager), popupController)
        ApplicationComponent.callbackToShowChooserPopup = Runnable {
            chooser.showPopup()
        }
    }

    override fun onDestroy() {
        ApplicationComponent.callbackToShowChooserPopup = null
        ApplicationComponent.callbackToCreateNewBoard = null
        super.onDestroy()
    }
}
