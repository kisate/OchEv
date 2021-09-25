package com.example.ochev

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.ochev.ui.GraphEditorsHolder
import com.example.ochev.ui.MordaViewPagerAdapter


class RTVRedactorMainActivity : FragmentActivity() {
    private val mainView: ViewPager2
        get() {
            return findViewById(R.id.main_activity_view)
        }

    private val graphEditorsHolder = GraphEditorsHolder()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_acitvity_view)

        val adapter = MordaViewPagerAdapter(this)
        mainView.adapter = adapter
        adapter.notifyDataSetChanged()
        mainView.isUserInputEnabled = false
    }
}
