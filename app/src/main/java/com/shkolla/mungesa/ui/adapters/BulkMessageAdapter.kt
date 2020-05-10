package com.shkolla.mungesa.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shkolla.mungesa.R
import com.shkolla.mungesa.databinding.BulkMessageItemBinding
import com.shkolla.mungesa.models.BulkMessage

class BulkMessageAdapter(private val interaction: BulkMessageInteraction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BulkMessage>() {

        override fun areItemsTheSame(oldItem: BulkMessage, newItem: BulkMessage): Boolean {
            TODO("not implemented")
        }

        override fun areContentsTheSame(oldItem: BulkMessage, newItem: BulkMessage): Boolean {
            TODO("not implemented")
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binder: BulkMessageItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.bulk_message_item,
            parent,
            false
        )

        return BulkMsgViewHolder(binder)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BulkMsgViewHolder -> {
                holder.binder.interaction = interaction
                holder.binder.bulkMessage = getBulkMessageAt(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private fun getBulkMessageAt(position: Int): BulkMessage {
        return differ.currentList[position]
    }

    fun submitList(list: List<BulkMessage>) {
        differ.submitList(null)
        differ.submitList(list)
    }

    class BulkMsgViewHolder
        (
        internal val binder: BulkMessageItemBinding
    ) : RecyclerView.ViewHolder(binder.root)

    interface BulkMessageInteraction {
        fun onBulkMessageSelected(messageItem: BulkMessage)
    }
}

