package com.example.ochev.ui

object ApplicationComponent {
    val viewersHolder: ViewersHolder = ViewersHolder()
    var callbackToCreateNewBoard: Runnable? = null
    var callbackToShowPopup: Runnable? = null
}