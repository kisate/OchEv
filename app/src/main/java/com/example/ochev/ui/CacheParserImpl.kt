package com.example.ochev.ui

import android.content.SharedPreferences
import com.example.ochev.baseclasses.cacheparser.CacheParser

class CacheParserImpl(spLazy: Lazy<SharedPreferences>) : CacheParser {
    private val sp by spLazy
    private var currentReading: Int = 1
    private var currentWriting: Int = 1

    override fun writeInt(int: Int) {
        sp.edit().putInt(currentWriting.toString(), int).apply()
        currentWriting++
    }

    override fun writeLong(long: Long) {
        sp.edit().putLong(currentWriting.toString(), long).apply()
        currentWriting++
    }

    override fun writeString(string: String) {
        sp.edit().putString(currentWriting.toString(), string).apply()
        currentWriting++
    }

    override fun writeFloat(float: Float) {
        sp.edit().putFloat(currentWriting.toString(), float).apply()
        currentWriting++
    }

    override fun readInt(): Int {
        return sp.getInt(currentReading.toString(), 0).also { currentReading++ }
    }

    override fun readLong(): Long {
        return sp.getLong(currentReading.toString(), 0).also { currentReading++ }
    }

    override fun readString(): String {
        return (sp.getString(currentReading.toString(), "") ?: "").also { currentReading++ }
    }

    override fun readFloat(): Float {
        return sp.getFloat(currentReading.toString(), 0.0f).also { currentReading++ }
    }

}
