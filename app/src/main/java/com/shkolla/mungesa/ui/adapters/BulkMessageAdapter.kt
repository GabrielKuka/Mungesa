package com.shkolla.mungesa.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shkolla.mungesa.R
import com.shkolla.mungesa.databinding.BulkMessageItemBinding
import com.shkolla.mungesa.models.BulkMessage

class BulkMessageAdapter(private val interaction: BulkMessageInteraction? = null) :
    RecyclerView.Adapter<BulkMessageAdapter.BulkMsgViewHolder>(), Filterable {

    private val bulkListAll: MutableList<BulkMessage> = mutableListOf()

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private fun getBulkMessageAt(position: Int): BulkMessage {
        return differ.currentList[position]
    }

    fun submitList(list: List<BulkMessage>?) {
        differ.submitList(null)
        differ.submitList(list)

        if (bulkListAll.isEmpty()) {
            list?.let { bulkListAll.addAll(it) }
        }
    }

    override fun getFilter(): Filter {
        return bulkFilter
    }

    private val bulkFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {

            val filteredList = mutableListOf<BulkMessage>()

            if (constraint == null || constraint == "") {
                filteredList.addAll(bulkListAll)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim()

                for (bulkItem in bulkListAll) {
                    if (bulkItem.studentName.toLowerCase().trim()
                            .contains(filterPattern)
                    ) {
                        filteredList.add(bulkItem)
                    }
                }

            }

            val results = FilterResults()
            results.values = filteredList

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            submitList(results?.values as List<BulkMessage>)
        }

    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BulkMessage>() {

        override fun areItemsTheSame(oldItem: BulkMessage, newItem: BulkMessage): Boolean {
            return oldItem.phoneNumber == newItem.phoneNumber
        }

        override fun areContentsTheSame(oldItem: BulkMessage, newItem: BulkMessage): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BulkMsgViewHolder {
        val binder: BulkMessageItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.bulk_message_item,
            parent,
            false
        )

        return BulkMsgViewHolder(binder)
    }

    override fun onBindViewHolder(holder: BulkMsgViewHolder, position: Int) {
        holder.binder.interaction = interaction
        holder.binder.bulkMessage = getBulkMessageAt(position)
    }

    class BulkMsgViewHolder
        (
        internal val binder: BulkMessageItemBinding
    ) : RecyclerView.ViewHolder(binder.root)

    interface BulkMessageInteraction {
        fun onBulkMessageSelected(messageItem: BulkMessage)
    }


}

