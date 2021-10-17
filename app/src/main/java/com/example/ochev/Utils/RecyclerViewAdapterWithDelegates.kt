package com.example.ochev.Utils

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.security.InvalidParameterException

class RecyclerViewAdapterWithDelegates(
    var items: List<Item>,
    private val delegates: List<Delegate>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates[viewType].onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        for (delegate in delegates) {
            if (delegate.match(items[position])) {
                delegate.onBindViewHolder(holder, items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        for (i in delegates.indices) {
            if (delegates[i].match(items[position])) {
                return i
            }
        }
        throw InvalidParameterException("Invalid position in getItemViewType in RecyclerViewAdapterWithDelegates")
    }
}