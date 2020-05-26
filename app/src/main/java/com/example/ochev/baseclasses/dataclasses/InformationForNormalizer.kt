package com.example.ochev.baseclasses.dataclasses

import android.graphics.Bitmap
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor
import com.example.ochev.ml.Classifier

data class InformationForNormalizer(
    val classifier: Classifier?,
    val bitmap: Bitmap?,
    val graphEditor: GraphEditor?,
    val strokes: MutableList<Stroke>?
)