package com.example.ochev.ui

import android.view.View
import android.view.ViewGroup
import com.example.ochev.baseclasses.editors.boardeditor.BoardViewer

interface CurrentGraphChanger {
    fun changeTo(viewer: BoardViewer)

    fun getViewToShowPopup(): ViewGroup
}