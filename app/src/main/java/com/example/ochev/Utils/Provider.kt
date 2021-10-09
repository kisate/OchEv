package com.example.ochev.Utils

fun interface Provider<T> {
    fun get(): T
}