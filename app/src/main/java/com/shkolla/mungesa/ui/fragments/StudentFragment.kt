package com.shkolla.mungesa.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.shkolla.mungesa.R
import com.shkolla.mungesa.models.Student
import com.shkolla.mungesa.ui.activities.Homepage
import com.shkolla.mungesa.ui.adapters.StudentAdapter
import com.shkolla.mungesa.viewmodels.StudentViewModel

class StudentFragment : Fragment(), StudentAdapter.StudentInteraction {

    private lateinit var navController: NavController
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var studentViewModel: StudentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        studentAdapter = StudentAdapter(this)


        val myView = inflater.inflate(R.layout.student_fragment, container, false)


        val recyclerView = myView.findViewById<RecyclerView>(R.id.student_rv)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = studentAdapter


        // Inflate the layout for this fragment
        return myView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        studentViewModel = (activity as Homepage).getViewModel()

        studentViewModel.getStudents().observe(viewLifecycleOwner, Observer {
            studentAdapter.submitList(it)
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

    }

    override fun onStudentSelected(student: Student) {
        studentViewModel.currentStudent(student)
        val bundle = bundleOf("currentStudent" to student)
        navController.navigate(R.id.action_studentFragment_to_daysFragment, bundle)

    }

}
