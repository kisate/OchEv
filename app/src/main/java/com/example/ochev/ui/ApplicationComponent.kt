package com.example.ochev.ui

object ApplicationComponent {
    val viewersHolder: ViewersHolder = ViewersHolder()
    var callbackToCreateNewBoard: Runnable? = null
    var callbackToShowChooserPopup: Runnable? = null
    var callbackToDeleteFragment: CloseFragmentCallback? = null
}
