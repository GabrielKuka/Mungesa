package com.shkolla.mungesa.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shkolla.mungesa.R
import com.shkolla.mungesa.databinding.DayItemBinding
import com.shkolla.mungesa.models.Day

class DayAdapter(private val interaction: DayInteraction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Day>() {

        override fun areItemsTheSame(oldItem: Day, newItem: Day): Boolean {
            return (oldItem.name == newItem.name) && (oldItem.isChecked == newItem.isChecked)
        }

        override fun areContentsTheSame(oldItem: Day, newItem: Day): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binder: DayItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.day_item,
            parent,
            false
        )

        return DayViewHolder(
            binder
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DayViewHolder -> {
                holder.binder.day = getDayAt(position)
                holder.binder.interaction = interaction
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private fun getDayAt(position: Int): Day {
        return differ.currentList[position]
    }

    fun submitList(list: List<Day>) {
        differ.submitList(list)
    }

    class DayViewHolder
        (
        internal val binder: DayItemBinding
    ) : RecyclerView.ViewHolder(binder.root)

    interface DayInteraction {
        fun onDaySelected(day: Day)
    }
}

