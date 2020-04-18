package com.shkolla.mungesa.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.ListFragment
import androidx.lifecycle.ViewModelProviders
import com.shkolla.mungesa.R
import com.shkolla.mungesa.models.Student
import com.shkolla.mungesa.repositories.StudentRepository
import com.shkolla.mungesa.ui.adapters.DaysListAdapter
import com.shkolla.mungesa.viewmodels.DaysViewModel
import kotlinx.android.synthetic.main.days_fragment.*


class DaysFragment : ListFragment(), AdapterView.OnItemClickListener {


    private var currentStudent: Student? = null
    lateinit var daysViewModel: DaysViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.days_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        currentStudent = StudentRepository.currentStudent

        daysViewModel = ViewModelProviders.of(this).get(DaysViewModel::class.java)

        daysViewModel.fillDaysList(this)

        listAdapter = DaysListAdapter(this, daysViewModel.getDaysList(), currentStudent!!)
        listView.onItemClickListener = this


        done_days.setOnClickListener {

            // get back to the students list
            (activity as AppCompatActivity).finish()

        }


    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }


}
