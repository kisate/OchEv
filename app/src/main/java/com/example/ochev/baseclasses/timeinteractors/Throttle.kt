package com.example.ochev.baseclasses.timeinteractors

import android.view.animation.AnimationUtils

class Throttle(private val mInterval: Long) {
    private var mLastFiredTimestamp: Long = 0
    fun attempt(runnable: Runnable) {
        if (hasSatisfiedInterval()) {
            runnable.run()
            mLastFiredTimestamp = now
        }
    }

    private fun hasSatisfiedInterval(): Boolean {
        val elapsed = now - mLastFiredTimestamp
        return elapsed >= mInterval
    }

    private val now: Long
        get() = AnimationUtils.currentAnimationTimeMillis()

}