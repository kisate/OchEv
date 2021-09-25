package com.example.ochev.callbacks

enum class UserMode {
    EDITING,
    DRAWING,
}

interface UserModeChangesListener {
    fun onUserModeChanged(userMode: UserMode)
}