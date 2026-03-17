package com.example.clauseclear.ui.compare

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.clauseclear.R
import com.example.clauseclear.data.model.ClauseComparison
import com.example.clauseclear.databinding.ItemComparisonRowBinding

class ComparisonRowAdapter :
    ListAdapter<ClauseComparison, ComparisonRowAdapter.ViewHolder>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<ClauseComparison>() {
            override fun areItemsTheSame(oldItem: ClauseComparison, newItem: ClauseComparison) =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: ClauseComparison, newItem: ClauseComparison) =
                oldItem == newItem
        }
    }

    inner class ViewHolder(val binding: ItemComparisonRowBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemComparisonRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = getItem(position)

        val oldText = item.clauseA?.plainEnglish ?: "Not Present"
        val newText = item.clauseB?.plainEnglish ?: "Not Present"

        holder.binding.tvOld.text = oldText
        holder.binding.tvNew.text = newText

        val changed = oldText != newText

        if (changed) {
            holder.binding.tvNew.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.red)
            )
        } else {
            holder.binding.tvNew.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.green)
            )
        }
    }
}
