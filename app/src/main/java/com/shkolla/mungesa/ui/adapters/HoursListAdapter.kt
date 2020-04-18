package com.shkolla.mungesa.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import androidx.fragment.app.ListFragment
import com.shkolla.mungesa.R
import com.shkolla.mungesa.models.CheckboxRow
import com.shkolla.mungesa.models.Student
import com.shkolla.mungesa.ui.fragments.HoursFragment
import java.util.*


class HoursListAdapter(
    private val c: ListFragment,
    private val list: MutableList<CheckboxRow>,
    val student: Student
) :
    BaseAdapter() {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val holder: RowViewHolder
        var view = convertView

        if (view != null) {
            holder = convertView?.tag as RowViewHolder
        } else {

            val inflater =
                c.activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            view = inflater.inflate(R.layout.listview_row, null, true)
            holder = RowViewHolder(view.findViewById(R.id.rowCheckBox))

            holder.checkBox.text = list[position].itemValue

            val currentDayPosition =
                (c as HoursFragment).daysViewModel.positionOfSelectedDay(student)

            if (student.selectedDays[currentDayPosition].selectedHours[position].isNotEmpty()) {
                holder.checkBox.isChecked = true
            }


            holder.checkBox.setOnClickListener {
                val selectedHoursList = student.selectedDays[currentDayPosition].selectedHours

                if (selectedHoursList[position].isEmpty()) {
                    selectedHoursList[position] = list[position].itemValue.toLowerCase(Locale.ROOT)
                } else {
                    selectedHoursList[position] = ""
                }
            }

            view.tag = holder
        }


        return view!!

    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }


    private inner class RowViewHolder(val checkBox: CheckBox)
}

