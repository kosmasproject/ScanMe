package com.kosmasfn.calculatorscan.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kosmasfn.calculatorscan.databinding.ItemResultBinding
import com.kosmasfn.calculatorscan.view.di.ResultModel

/**
 * Created by Kosmas on September 01, 2023.
 */
class ResultListAdapter : ListAdapter<ResultModel.CalculationData, ViewHolder>(ResultDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            getItem(position)?.let {
                holder.bind(it)
            }
        }
    }

    private inner class ViewHolder(val binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResultModel.CalculationData) {
            with(binding) {
                tvInput.text = item.input
                tvResult.text = item.result
            }
        }
    }

    class ResultDiffCallback : DiffUtil.ItemCallback<ResultModel.CalculationData>() {
        override fun areItemsTheSame(
            oldItem: ResultModel.CalculationData,
            newItem: ResultModel.CalculationData
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: ResultModel.CalculationData,
            newItem: ResultModel.CalculationData
        ): Boolean = oldItem.hashCode() == newItem.hashCode()
    }
}