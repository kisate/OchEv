package com.example.ochev.baseclasses.cacheparser

import android.os.Parcelable
import kotlin.reflect.KClass

interface CacheParser {
    fun writeInt(int: Int)

    fun writeLong(long: Long)

    fun writeString(string: String)

    fun writeFloat(float: Float)

    fun writeParcelable(parcel: Parcelable)

    fun readInt(): Int

    fun readLong(): Long

    fun readString(): String

    fun readFloat(): Float

    fun getFieldCount(): Int

    fun getParcelable(cl: Class<out Parcelable>): Parcelable?

    fun closeTransaction()
}