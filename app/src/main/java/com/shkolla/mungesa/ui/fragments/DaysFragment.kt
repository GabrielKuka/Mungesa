package com.shkolla.mungesa.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.shkolla.mungesa.R
import com.shkolla.mungesa.models.Day
import com.shkolla.mungesa.models.Student
import com.shkolla.mungesa.ui.activities.Homepage
import com.shkolla.mungesa.ui.adapters.DayAdapter
import com.shkolla.mungesa.viewmodels.StudentViewModel


class DaysFragment : Fragment(), DayAdapter.DayInteraction {

    private lateinit var navController: NavController
    private lateinit var studentViewModel: StudentViewModel
    private lateinit var dayAdapter: DayAdapter
    private lateinit var currentStudent: Student

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentStudent = requireArguments().getParcelable("currentStudent")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.days_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        // setup recyclerview
        val days_rv = view.findViewById<RecyclerView>(R.id.days_rv)
        dayAdapter = DayAdapter(this)
        days_rv.adapter = dayAdapter


        studentViewModel = (activity as Homepage).getViewModel()

        studentViewModel.getDays(currentStudent).observe(
            viewLifecycleOwner, Observer {
                dayAdapter.submitList(it)
            }
        )

        // setup done button
        val doneDays = view.findViewById<Button>(R.id.done_days)
        doneDays.setOnClickListener {
            // select this student
            studentViewModel.addSelectedDays(currentStudent)
            // go back
            requireActivity().onBackPressed()
        }
    }


    override fun onDaySelected(day: Day) {
        studentViewModel.currentDay(day)
        val bundle = bundleOf(
            "currentDay" to day,
            "currentStudent" to currentStudent
        )
        navController.navigate(R.id.action_daysFragment_to_hoursFragment, bundle)

    }

}
