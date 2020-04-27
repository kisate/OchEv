package com.example.ochev.baseclasses.dataclasses

import com.example.ochev.ml.Classifier
import com.example.ochev.viewclasses.DrawStrokeView

data class InfrormationForNormalizer(
    val classifier: Classifier?,
    val view: DrawStrokeView?,
    val graph: Graph?,
    val strokes: MutableList<Stroke>?
)