package com.shkolla.mungesa.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shkolla.mungesa.R
import com.shkolla.mungesa.helpers.SelectMode
import com.shkolla.mungesa.interfaces.SelectedStudentListener
import com.shkolla.mungesa.viewmodels.HomepageViewModel
import kotlinx.android.synthetic.main.recycler_view_item.view.*

class StudentListAdapter(
    private val selectedStudentListener: SelectedStudentListener,
    private val hpViewModel: HomepageViewModel
) :
    RecyclerView.Adapter<ViewHolder>() {

    private val students = hpViewModel.getStudents()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
            , selectedStudentListener
        )
    }

    override fun getItemCount(): Int {
        return students.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.studentFullName.text =
            students[position].firstName.plus(" ").plus(students[position].lastName)
        holder.studentPhoneNumber.text = students[position].phoneNumber

        if (SelectMode.isEnabled()) {
            holder.checkImage.visibility = View.VISIBLE
            if (hpViewModel.isStudentSelected(students[position])) {
                holder.checkImage.setBackgroundResource(R.drawable.ic_checked)
            } else {
                holder.checkImage.setBackgroundResource(R.drawable.ic_unchecked)
            }
        } else {
            holder.checkImage.visibility = View.INVISIBLE

        }

    }


}

class ViewHolder(view: View, private val selectedStudentListener: SelectedStudentListener) :
    RecyclerView.ViewHolder(view),
    View.OnClickListener {
    val studentFullName: TextView = view.full_name_text
    val studentPhoneNumber: TextView = view.phone_number_text
    val checkImage: ImageView = view.check_image


    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        selectedStudentListener.onSelectedStudent(adapterPosition)
    }

}