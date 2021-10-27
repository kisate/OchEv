package com.example.ochev

import android.app.Application
import android.content.SharedPreferences
import com.example.ochev.ui.ApplicationComponent
import com.example.ochev.ui.CacheParserImpl

class RTVRedactorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        raiseCache()
    }

    private fun raiseCache() {
        if (ApplicationComponent.viewersHolder.isEmpty()) {
            ApplicationComponent.viewersHolder.createAndAddNewViewer(this)
        }

        var current = 1
        while(true) {
            val sp = getGraphSp(current)
            if (!sp.contains("present")) {
                break
            }
            current++

            ApplicationComponent.viewersHolder.createAndAddNewViewer(applicationContext, CacheParserImpl(sp))
        }
    }

    private fun getGraphSp(index: Int): SharedPreferences {
        return getSharedPreferences("viewer$index", MODE_PRIVATE)
    }
}