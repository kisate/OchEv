package com.example.ochev.baseclasses.dataclasses

import android.graphics.Bitmap
import com.example.ochev.ml.Classifier

data class InformationForNormalizer(
    val classifier: Classifier?,
    val bitmap: Bitmap?,
    val graph: Graph?,
    val strokes: MutableList<Stroke>?
)