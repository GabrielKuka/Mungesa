package com.shkolla.mungesa.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.shkolla.mungesa.R
import com.shkolla.mungesa.models.Day
import com.shkolla.mungesa.models.Hour
import com.shkolla.mungesa.models.Student
import com.shkolla.mungesa.ui.activities.Homepage
import com.shkolla.mungesa.ui.adapters.HourAdapter
import com.shkolla.mungesa.viewmodels.StudentViewModel

class HoursFragment : Fragment(), HourAdapter.HourInteraction {

    private lateinit var navController: NavController
    private lateinit var studentViewModel: StudentViewModel
    private lateinit var hourAdapter: HourAdapter

    private lateinit var currentStudent: Student
    private lateinit var currentDay: Day

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentStudent = requireArguments().getParcelable("currentStudent")!!
        currentDay = requireArguments().getParcelable("currentDay")!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.hours_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        // setup recyclerview
        val hour_rv = view.findViewById<RecyclerView>(R.id.hour_rv)
        hourAdapter = HourAdapter(this)

        hour_rv.adapter = hourAdapter


        studentViewModel = (activity as Homepage).getViewModel()

        studentViewModel.getHours(currentDay).observe(viewLifecycleOwner, Observer {
            hourAdapter.submitList(it)
        })

        // setup button
        val doneHours = view.findViewById<Button>(R.id.done_hours)
        doneHours.setOnClickListener {
            // 1. pass the temp hour list as argument to the days fragment
            studentViewModel.addSelectedHours(currentDay)

            // 2. go back
            requireActivity().onBackPressed()
        }
    }

    override fun onHourSelected(hour: Hour) {
        // select an hour
        studentViewModel.selectHour(currentDay, hour)
    }

}
