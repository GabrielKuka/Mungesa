package com.shkolla.mungesa.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.ListFragment
import androidx.lifecycle.ViewModelProviders
import com.shkolla.mungesa.R
import com.shkolla.mungesa.models.Day
import com.shkolla.mungesa.repositories.DaysAndHoursRepository
import com.shkolla.mungesa.repositories.StudentRepository
import com.shkolla.mungesa.ui.adapters.HoursListAdapter
import com.shkolla.mungesa.viewmodels.DaysViewModel
import kotlinx.android.synthetic.main.hours_fragment.*


class HoursFragment : ListFragment(), AdapterView.OnItemClickListener {


    lateinit var daysViewModel: DaysViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.hours_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        daysViewModel = ViewModelProviders.of(this).get(DaysViewModel::class.java)

        val thisFragment = this

        listAdapter =
            HoursListAdapter(this, daysViewModel.getHoursList(), StudentRepository.currentStudent!!)
        listView.onItemClickListener = this


        daysViewModel.fillHoursList(this)

        done_hours.setOnClickListener {
            // if there are no hours selected, deselect this day
            if (daysViewModel.getHoursList().size - daysViewModel.getNumberOfSelectedHours(
                    DaysAndHoursRepository.currentDay!!
                ) == 0
            ) {

                StudentRepository.currentStudent!!.selectedDays[daysViewModel.positionOfSelectedDay(
                    StudentRepository.currentStudent!!
                )] =
                    Day()

            }


            val fragmentManager = activity?.supportFragmentManager
            val transaction = fragmentManager?.beginTransaction()


            transaction?.apply {
                setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                remove(thisFragment) }?.commit()

            with(fragmentManager) { this?.popBackStackImmediate() }


        }

    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }


}
