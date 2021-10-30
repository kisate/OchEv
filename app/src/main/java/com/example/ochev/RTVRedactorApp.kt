package com.example.ochev

import android.app.Application
import android.content.SharedPreferences
import com.example.ochev.ui.ApplicationComponent
import com.example.ochev.ui.CacheParserImpl

class RTVRedactorApp : Application() {
    override fun onCreate() {
        raiseCache()
        super.onCreate()
    }

    private fun raiseCache() {
        val cnt = getAppSp().getInt("graph count", 0)
        for (current in 1..cnt) {
            ApplicationComponent.viewersHolder.createAndAddNewViewer(
                applicationContext, CacheParserImpl(
                    lazy { getGraphSp(current) })
            )
        }

        if (ApplicationComponent.viewersHolder.isEmpty()) {
            ApplicationComponent.viewersHolder.createAndAddNewViewer(this)
        }
    }

    private fun getGraphSp(index: Int): SharedPreferences {
        return getSharedPreferences("viewer$index", MODE_PRIVATE)
    }

    private fun getAppSp(): SharedPreferences {
        return getSharedPreferences("app", MODE_PRIVATE)
    }
}