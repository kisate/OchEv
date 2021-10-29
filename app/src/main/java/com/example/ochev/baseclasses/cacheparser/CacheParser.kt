package com.example.ochev.baseclasses.cacheparser

interface CacheParser {
    fun writeInt(int: Int)

    fun writeLong(long: Long)

    fun writeString(string: String)

    fun writeFloat(float: Float)

    fun readInt(): Int

    fun readLong(): Long

    fun readString(): String

    fun readFloat(): Float

    fun getFieldCount(): Int
}