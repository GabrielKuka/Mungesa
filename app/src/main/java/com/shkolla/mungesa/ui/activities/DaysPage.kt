package com.shkolla.mungesa.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.shkolla.mungesa.R
import com.shkolla.mungesa.models.Day
import com.shkolla.mungesa.models.Student
import com.shkolla.mungesa.repos.AppData
import com.shkolla.mungesa.ui.adapters.DayAdapter
import com.shkolla.mungesa.viewmodels.DayViewModel
import kotlinx.android.synthetic.main.day_page.*

class DaysPage : AppCompatActivity(), DayAdapter.DayInteraction {

    private lateinit var dayAdapter: DayAdapter
    private lateinit var dayViewModel: DayViewModel
    private lateinit var currentStudent: Student

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.day_page)

        currentStudent = intent.extras?.getParcelable("currentStudent")!!
        title = currentStudent.firstName + " " + currentStudent.lastName

        initRecyclerView()

        dayViewModel = ViewModelProvider(this).get(DayViewModel::class.java)
        dayViewModel.initDays(currentStudent)

        dayViewModel.getDays().observe(this, Observer {
            dayAdapter.submitList(it)
        })

        done_days.setOnClickListener {
            val intent = Intent()
            intent.putExtra("currentStudent", currentStudent)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun initRecyclerView() {
        dayAdapter = DayAdapter(this)
        days_rv.apply {
            adapter = dayAdapter
            addItemDecoration(DividerItemDecoration(this@DaysPage, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onDaySelected(day: Day) {

        dayViewModel.currentDay(day)
        val i = Intent(this, HoursPage::class.java)
        i.putExtra("currentDay", day)
        startActivityForResult(i, AppData.SELECT_DAY_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AppData.SELECT_DAY_REQUEST && resultCode == Activity.RESULT_OK) {
            // select day
            val currentDay = data?.extras?.getParcelable<Day>("currentDay")!!

            dayViewModel.selectDay(currentDay)

        }
    }
}
