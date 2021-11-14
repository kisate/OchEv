package com.example.ochev.ui

data class Gesture(
    val type: GestureType = GestureType.NONE,
    val state: GestureState = GestureState.NONE
) {
}

enum class GestureType {
    NONE,
    TAP,
    MOVE,
    SCROLL_AND_ZOOM,
    LONG_TAP;
}

enum class GestureState {
    NONE,
    START,
    IN_PROGRESS,
    END;
}