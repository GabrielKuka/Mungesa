package com.shkolla.mungesa.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shkolla.mungesa.R
import com.shkolla.mungesa.databinding.HourItemBinding
import com.shkolla.mungesa.models.Hour

class HourAdapter(private val interaction: HourInteraction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Hour>() {

        override fun areItemsTheSame(oldItem: Hour, newItem: Hour): Boolean {
            return (oldItem.name == newItem.name) && (oldItem.isChecked == newItem.isChecked)
        }

        override fun areContentsTheSame(oldItem: Hour, newItem: Hour): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binder: HourItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.hour_item,
            parent,
            false
        )

        return HourViewHolder(
            binder
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HourViewHolder -> {
                holder.binder.hour = getHourAt(position)
                holder.binder.interaction = interaction
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private fun getHourAt(position: Int): Hour {
        return differ.currentList[position]
    }

    fun submitList(list: List<Hour>) {
        differ.submitList(null)
        differ.submitList(list)
    }

    class HourViewHolder
        (
        internal val binder: HourItemBinding
    ) : RecyclerView.ViewHolder(binder.root)

    interface HourInteraction {
        fun onHourSelected(hour: Hour)
    }
}

