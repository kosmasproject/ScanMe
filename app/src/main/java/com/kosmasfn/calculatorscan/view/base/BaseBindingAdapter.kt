package com.kosmasfn.calculatorscan.view.base

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Created by Kosmas on September 01, 2023.
 */
abstract class BaseBindingAdapter<T : BaseBindingViewHolder> : RecyclerView.Adapter<T>() {

    private lateinit var context: Context

    override fun onBindViewHolder(holder: T, position: Int) {
        context = holder.binding.root.context
        updateBinding(holder, holder.binding, position)
    }

    protected abstract fun updateBinding(holder: T, binding: ViewBinding, position: Int)

}