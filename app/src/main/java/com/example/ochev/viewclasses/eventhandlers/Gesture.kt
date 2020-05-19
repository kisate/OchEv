package com.example.ochev.viewclasses.eventhandlers

data class Gesture(
    val type: GestureType = GestureType.NONE,
    val state: GestureState = GestureState.NONE
) {
}

enum class GestureType {
    NONE,
    TAP,
    MOVE,
    SCROLL;
}

enum class GestureState {
    NONE,
    START,
    IN_PROGRESS,
    END;
}