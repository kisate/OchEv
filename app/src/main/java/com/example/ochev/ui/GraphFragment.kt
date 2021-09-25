package com.example.ochev.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ochev.R
import com.example.ochev.baseclasses.editors.grapheditor.GraphEditor

class GraphFragment(
    private val graphEditor: GraphEditor
) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.graph_fragment_view, container)
    }
}