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
import com.shkolla.mungesa.databinding.StudentItemBinding
import com.shkolla.mungesa.models.Student

class StudentAdapter(private val interaction: StudentInteraction? = null) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>(), Filterable {

    private val studentsListAll: MutableList<Student> = mutableListOf()

    private fun getStudentAt(position: Int): Student {
        return differ.currentList[position]
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Student>?) {
        differ.submitList(null)
        differ.submitList(list)

        if (studentsListAll.isEmpty()) {
            list?.let { studentsListAll.addAll(it) }
        }
    }


    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Student>() {

        override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
            return (oldItem.firstName + oldItem.lastName) == (newItem.firstName + newItem.lastName)
        }

        override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {

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

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {

        holder.binder.student = getStudentAt(position)
        holder.binder.interaction = interaction

    }

    class StudentViewHolder(
        internal val binder: StudentItemBinding
    ) : RecyclerView.ViewHolder(binder.root)

    interface StudentInteraction {
        fun onStudentSelected(student: Student)
    }

    override fun getFilter(): Filter {
        return studentFilter
    }

    private val studentFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<Student>()

            if (constraint == null || constraint == "") {
                filteredList.addAll(studentsListAll)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim()
                for (student in studentsListAll) {
                    if (student.firstName matches filterPattern || student.lastName matches filterPattern || student.phoneNumber matches filterPattern) {
                        filteredList.add(student)
                    }
                }
            }

            val results = FilterResults()
            results.values = filteredList

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            submitList(results?.values as List<Student>)
        }

        private infix fun String.matches(text: String): Boolean {
            return this.toLowerCase().trim().contains(text)
        }
    }
}

