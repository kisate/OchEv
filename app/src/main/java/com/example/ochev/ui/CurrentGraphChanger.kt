package com.example.ochev.ui

import android.view.View
import android.view.ViewGroup
import com.example.ochev.baseclasses.editors.boardeditor.BoardViewer

fun interface CurrentGraphChanger {
    fun changeTo(id: String)
}