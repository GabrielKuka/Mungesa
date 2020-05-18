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
import com.shkolla.mungesa.models.Hour
import com.shkolla.mungesa.ui.adapters.HourAdapter
import com.shkolla.mungesa.viewmodels.HourViewModel
import kotlinx.android.synthetic.main.hour_page.*

class HoursPage : AppCompatActivity(), HourAdapter.HourInteraction {

    private lateinit var hourAdapter: HourAdapter
    private lateinit var currentDay: Day
    private lateinit var hourViewModel: HourViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hour_page)

        currentDay = intent.extras?.getParcelable("currentDay")!!

        initRecyclerView()

        title = "Orët për ditën " + currentDay.name.toLowerCase()

        hourViewModel = ViewModelProvider(this).get(HourViewModel::class.java)
        hourViewModel.initHours(currentDay)
        hourViewModel.getHours().observe(this, Observer {
            hourAdapter.submitList(it)
        })

        done_hours.setOnClickListener {
            val intent = Intent()
            intent.putExtra("currentDay", currentDay)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun initRecyclerView() {
        hourAdapter = HourAdapter(this)
        hours_rv.apply {
            adapter = hourAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@HoursPage,
                    DividerItemDecoration.VERTICAL
                )
            )
            setHasFixedSize(true)
        }
    }

    override fun onHourSelected(position: Int, hour: Hour) {
        hourViewModel.selectHour(hour)
        hourAdapter.notifyItemChanged(position)
    }
}
