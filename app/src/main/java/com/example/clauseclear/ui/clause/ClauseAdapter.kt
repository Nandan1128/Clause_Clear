package com.example.clauseclear.ui.clause

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.clauseclear.R
import com.example.clauseclear.data.model.Clause
import com.example.clauseclear.data.model.RiskLevel
import com.example.clauseclear.databinding.ItemClauseBinding

class ClauseAdapter(
    private val onClick: (Clause) -> Unit
) : RecyclerView.Adapter<ClauseAdapter.ClauseViewHolder>() {

    private val items = mutableListOf<Clause>()

    fun submitList(list: List<Clause>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class ClauseViewHolder(
        val binding: ItemClauseBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClauseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemClauseBinding.inflate(inflater, parent, false)
        return ClauseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClauseViewHolder, position: Int) {
        val item = items[position]
        val context = holder.itemView.context

        holder.binding.tvClauseName.text = item.title
        holder.binding.tvClausePreview.text = item.originalText.take(80)

        when (item.riskLevel) {
            RiskLevel.HIGH -> {
                holder.binding.tvRiskBadge.text = "High Risk"
                holder.binding.tvRiskBadge.setTextColor(ContextCompat.getColor(context, R.color.red))
                holder.binding.tvRiskBadge.setBackgroundResource(R.drawable.badge_red)
                holder.binding.viewDot.setBackgroundResource(R.drawable.circle_red)
            }
            RiskLevel.MEDIUM -> {
                holder.binding.tvRiskBadge.text = "Medium"
                holder.binding.tvRiskBadge.setTextColor(ContextCompat.getColor(context, R.color.yellow))
                holder.binding.tvRiskBadge.setBackgroundResource(R.drawable.badge_yellow)
                holder.binding.viewDot.setBackgroundResource(R.drawable.circle_yellow)
            }
            RiskLevel.INFO -> {
                holder.binding.tvRiskBadge.text = "Info"
                holder.binding.tvRiskBadge.setTextColor(ContextCompat.getColor(context, R.color.green))
                holder.binding.tvRiskBadge.setBackgroundResource(R.drawable.badge_green)
                holder.binding.viewDot.setBackgroundResource(R.drawable.circle_green)
            }
        }

        holder.binding.root.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount() = items.size
}
