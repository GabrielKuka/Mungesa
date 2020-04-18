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
import com.shkolla.mungesa.models.Day
import com.shkolla.mungesa.models.Student
import com.shkolla.mungesa.ui.fragments.DaysFragment
import com.shkolla.mungesa.ui.fragments.HoursFragment
import java.util.*


class DaysListAdapter(
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
                c.activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            view = inflater.inflate(R.layout.listview_row, null, true)
            holder = RowViewHolder(view.findViewById(R.id.rowCheckBox))
            view.tag = holder


            if (student.selectedDays[position].name.isNotEmpty()) {
                holder.checkBox.isChecked = true
            }

            holder.checkBox.text = list[position].itemValue
            holder.checkBox.setOnClickListener {

                if (student.selectedDays[position].name.isEmpty()) {
                    // Select this day
                    student.selectedDays[position].name = list[position].itemValue.toLowerCase(
                        Locale.ROOT
                    )

                    (c as DaysFragment).daysViewModel.setCurrentDay(Day())
                    c.daysViewModel.getCurrentDay()?.name =
                        list[position].itemValue.toLowerCase(Locale.ROOT)

                    // Launch hours fragment
                    val fragmentManager = c.activity?.supportFragmentManager
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    transaction?.hide(c)
                    transaction?.add(R.id.contentFragment, HoursFragment(), "hoursFragment")
                        ?.addToBackStack("daysFragment")
                        ?.commit()
                } else {

                    // remove selected hours from this day
                    student.selectedDays[position].selectedHours =
                        mutableListOf("", "", "", "", "", "", "")

                    // remove selected day
                    student.selectedDays[position] = Day()
                    (c as DaysFragment).daysViewModel.setCurrentDay(null)

                }

            }

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

