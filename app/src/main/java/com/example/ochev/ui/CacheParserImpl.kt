package com.example.ochev.ui

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Parcelable
import com.example.ochev.baseclasses.cacheparser.CacheParser
import com.google.gson.Gson
import kotlin.reflect.KClass

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

    override fun <T : Parcelable> writeParcelable(parcel: T) {
        val json = Gson().toJson(parcel)
        sp.edit().putString(currentWriting.toString(), json).apply()
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

    override fun getFieldCount(): Int {
        return sp.getInt("cnt", 0)
    }

    override fun getParcelable(cl: Class<Parcelable>): Parcelable? {
        val json = sp.getString(currentReading.toString(), null)
        currentReading++
        json ?: return null
        return Gson().fromJson(json, cl)
    }

    override fun closeTransaction() {
        sp.edit().putInt("cnt", currentWriting - 1).apply()
    }

}
