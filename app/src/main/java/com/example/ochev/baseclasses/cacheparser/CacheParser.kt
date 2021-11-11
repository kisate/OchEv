package com.example.ochev.baseclasses.cacheparser

import android.os.Parcelable

interface CacheParser {
    fun writeInt(int: Int)

    fun writeLong(long: Long)

    fun writeString(string: String)

    fun writeFloat(float: Float)

    fun <T : Parcelable> writeParcelable(parcel: T)

    fun readInt(): Int

    fun readLong(): Long

    fun readString(): String

    fun readFloat(): Float

    fun getFieldCount(): Int

    fun getParcelable(cl: Class<Parcelable>): Parcelable?

    fun closeTransaction()
}