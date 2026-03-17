package com.example.clauseclear.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clauseclear.data.local.entity.DocumentEntity
import com.example.clauseclear.databinding.ItemDocumentBinding

class DocumentAdapter(
    private val onClick: (DocumentEntity) -> Unit
) : RecyclerView.Adapter<DocumentAdapter.DocViewHolder>() {

    private val items = mutableListOf<DocumentEntity>()

    fun submitList(list: List<DocumentEntity>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class DocViewHolder(
        val binding: ItemDocumentBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocViewHolder {
        val binding = ItemDocumentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DocViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DocViewHolder, position: Int) {
        val item = items[position]

        holder.binding.tvFileName.text = item.fileName
        holder.binding.tvMeta.text =
            "${item.pageCount} pages • ${item.clauseCount} clauses"

        holder.binding.root.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount() = items.size
}
