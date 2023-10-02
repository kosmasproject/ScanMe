package com.kosmasfn.calculatorscan.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.kosmasfn.calculatorscan.databinding.ItemResultBinding
import com.kosmasfn.calculatorscan.view.base.BaseBindingAdapter
import com.kosmasfn.calculatorscan.view.base.BaseBindingViewHolder
import com.kosmasfn.calculatorscan.view.di.ResultModel

/**
 * Created by Kosmas on September 01, 2023.
 */
class ResultAdapter : BaseBindingAdapter<BaseBindingViewHolder>() {

    private val items = mutableListOf<ResultModel.CalculationData>()

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(list: List<ResultModel.CalculationData>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder {
        return BaseBindingViewHolder(
            ItemResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun updateBinding(holder: BaseBindingViewHolder, binding: ViewBinding, position: Int) {
        with(binding as ItemResultBinding) {
            tvInput.text = items[position].input
            tvResult.text = items[position].result
        }
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int = items.size
}