package com.example.ochev.callbacks

enum class UserMode {
    EDITING,
    DRAWING,
}

fun interface UserModeChangesListener {
    fun onUserModeChanged(userMode: UserMode)
}