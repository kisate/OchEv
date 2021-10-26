package com.example.ochev

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.ochev.ui.ApplicationComponent
import com.example.ochev.ui.CloseFragmentCallback
import com.example.ochev.ui.MordaViewPagerAdapter
import com.example.ochev.ui.PopupController
import com.example.ochev.ui.graphchooser.CurrentGraphChangerImpl
import com.example.ochev.ui.graphchooser.GraphChooserController


class RTVRedactorMainActivity : FragmentActivity() {
    private val mPager: ViewPager2
        get() {
            return findViewById(R.id.main_activity_pager)
        }

    private lateinit var popupController: PopupController

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
        ApplicationComponent.callbackToDeleteFragment = CloseFragmentCallback {
            ApplicationComponent.viewersHolder.deleteViewer(it)
            mPager.adapter = null

            if (adapter.itemCount == 0) {
                ApplicationComponent.viewersHolder.createAndAddNewViewer(this)
            }

            mPager.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        mPager.adapter = adapter
        adapter.notifyDataSetChanged()
        mPager.isUserInputEnabled = false
    }

    private fun initPopups() {
        popupController =
            PopupController(findViewById(R.id.popup_window), findViewById(R.id.popup_container))
        val chooser = GraphChooserController(this, CurrentGraphChangerImpl(mPager), popupController)
        ApplicationComponent.callbackToShowChooserPopup = Runnable {
            chooser.showPopup()
        }
    }

    override fun onDestroy() {
        ApplicationComponent.callbackToShowChooserPopup = null
        ApplicationComponent.callbackToCreateNewBoard = null
        ApplicationComponent.callbackToDeleteFragment = null
        if (!isChangingConfigurations) {
            popupController.dismissPopup()
        } else {
            popupController.endAnim()
        }
        super.onDestroy()
    }
}
