package com.example.ochev.callbacks

enum class UserMode {
    EDITING, // deprecated
    /* need to know which shape is being edited in order not to show copying */
    EDITING__COPY_ENABLED,
    EDITING__COPY_DISABLED,
    DRAWING,
}