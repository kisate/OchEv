package com.example.ochev.Utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

class Utils {
}

val Number.toPx get() = TypedValue.applyDimension(
  TypedValue.COMPLEX_UNIT_DIP,
  this.toFloat(),
  Resources.getSystem().displayMetrics)