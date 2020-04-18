package com.shkolla.mungesa.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.shkolla.mungesa.R
import com.shkolla.mungesa.models.BulkMessage
import kotlinx.android.synthetic.main.bulksms_item.view.*
import kotlinx.android.synthetic.main.recycler_view_item.view.*

class BulkSmsAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BulkMessage>() {

        override fun areItemsTheSame(oldItem: BulkMessage, newItem: BulkMessage): Boolean {
            return oldItem.fullName == newItem.fullName
        }

        override fun areContentsTheSame(oldItem: BulkMessage, newItem: BulkMessage): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return BulkSmsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.bulksms_item,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BulkSmsViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<BulkMessage>) {
        differ.submitList(list)
    }

    class BulkSmsViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: BulkMessage) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            itemView.full_name_bulk.text = item.fullName
            itemView.message_bulk.text = item.message
            itemView.phone_number_bulk.text = item.phoneNumber


        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: BulkMessage)
    }
}

