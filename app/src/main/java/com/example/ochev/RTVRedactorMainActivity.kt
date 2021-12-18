package com.example.ochev

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.ochev.ui.*
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
        val adapter = MordaViewPagerAdapter(this)
        ApplicationComponent.callbackToCreateNewBoard = Runnable {
            val count = adapter.itemCount
            adapter.createAndAddNewFragment()
            adapter.notifyItemInserted(count)
            mPager.setCurrentItem(adapter.itemCount - 1, true)
        }
        ApplicationComponent.callbackToDeleteFragment = CloseFragmentCallback {
            ApplicationComponent.viewersHolder.deleteViewer(it) ?: return@CloseFragmentCallback
            if (adapter.itemCount == 0) {
                ApplicationComponent.viewersHolder.createAndAddNewViewer(applicationContext)
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

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "on stop")
        writeCaches()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "on destroy")
        ApplicationComponent.callbackToShowChooserPopup = null
        ApplicationComponent.callbackToCreateNewBoard = null
        ApplicationComponent.callbackToDeleteFragment = null

        if (!isChangingConfigurations) {
            popupController.dismissPopup()
        } else {
            popupController.endAnim()
        }
    }

    private fun writeCaches() {
        var current = 1
        for (entry in ApplicationComponent.viewersHolder.entries()) {
            val sp = getSp(current)
            sp.edit().clear().apply()
            entry.value.saveInCache(CacheParserImpl(lazy { sp }))
            sp.edit().putBoolean("present", true).apply()
            current++
        }
        joinAll()
        getAppSp().edit().putInt("graph count", current - 1).apply()
    }

    private fun joinAll() {
        ApplicationComponent.viewersHolder.entries().stream().map {
            it.value.join()
        }
    }

    private fun getSp(index: Int): SharedPreferences {
        return getSharedPreferences("viewer$index", MODE_PRIVATE)
    }

    private fun getAppSp(): SharedPreferences {
        return getSharedPreferences("app", MODE_PRIVATE)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    companion object {
        private val TAG = "RTVRedactorMainActivity"
    }
}
