package com.example.ochev

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.ochev.ui.ApplicationComponent
import com.example.ochev.ui.MordaViewPagerAdapter


class RTVRedactorMainActivity : FragmentActivity() {
    private val mainView: ViewPager2
        get() {
            return findViewById(R.id.main_activity_view)
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
            mainView.setCurrentItem(adapter.itemCount - 1, true)
        }
        mainView.adapter = adapter
        adapter.notifyDataSetChanged()
        mainView.isUserInputEnabled = false
    }

    override fun onDestroy() {
        ApplicationComponent.callbackToCreateNewBoard = null
        super.onDestroy()
    }
}
