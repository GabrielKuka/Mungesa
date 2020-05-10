package com.shkolla.mungesa.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shkolla.mungesa.R
import com.shkolla.mungesa.databinding.StudentItemBinding
import com.shkolla.mungesa.models.Student

class StudentAdapter(private val interaction: StudentInteraction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Student>() {

        override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
            return (oldItem.firstName + oldItem.lastName) == (newItem.firstName + newItem.lastName)
        }

        override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binder: StudentItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.student_item,
            parent,
            false
        )

        return StudentViewHolder(
            binder
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is StudentViewHolder -> {
                holder.binder.student = getStudentAt(position)
                holder.binder.interaction = interaction

            }
        }

    }

    private fun getStudentAt(position: Int): Student {
        return differ.currentList[position]
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Student>) {
        differ.submitList(null)
        differ.submitList(list)
    }

    class StudentViewHolder
    constructor(
        internal val binder: StudentItemBinding
    ) : RecyclerView.ViewHolder(binder.root)

    interface StudentInteraction {
        fun onStudentSelected(student: Student)
    }
}

