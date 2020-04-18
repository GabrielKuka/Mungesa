package com.shkolla.mungesa.viewmodels


import androidx.fragment.app.ListFragment
import androidx.lifecycle.ViewModel
import com.shkolla.mungesa.R
import com.shkolla.mungesa.models.CheckboxRow
import com.shkolla.mungesa.models.Day
import com.shkolla.mungesa.models.Student
import com.shkolla.mungesa.repositories.DaysAndHoursRepository
import com.shkolla.mungesa.repositories.StudentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DaysViewModel : ViewModel() {

    private var daysList: MutableList<CheckboxRow> = ArrayList()
    private var hoursList: MutableList<CheckboxRow> = ArrayList()


    fun fillDaysList(ac: ListFragment) {
        CoroutineScope(Dispatchers.Default).launch {
            for (pos in 0..4) {
                daysList.add(CheckboxRow(false, ac.resources.getStringArray(R.array.days)[pos]))
            }
        }

    }

    fun fillHoursList(ac: ListFragment) {
        CoroutineScope(Dispatchers.Default).launch {

            with(hoursList) {
                for (pos in 0..6)
                    add(
                        CheckboxRow(
                            false,
                            ac.resources.getStringArray(R.array.hours)[pos]
                        )
                    )
            }
        }
    }

    fun getDaysList(): MutableList<CheckboxRow> = daysList
    fun getHoursList(): MutableList<CheckboxRow> = hoursList

    fun getCurrentDay(): Day? = DaysAndHoursRepository.currentDay

    fun setCurrentDay(day: Day?) {
        DaysAndHoursRepository.currentDay = day
    }

    val positionOfSelectedDay: (Student) -> Int =
        { s -> s.selectedDays.indexOf(DaysAndHoursRepository.currentDay) }


    fun getNumberOfSelectedHours(day: Day): Int {
        var counter = 0


        for (hour in day.selectedHours)
            if (hour.isNotEmpty())
                counter++


        return counter
    }


    fun checkForSelectedHours() {
        CoroutineScope(Dispatchers.Default).launch {
            if (getNumberOfSelectedHours(getCurrentDay()!!) == 0) {
                // if the number of selected hours is 0, then deselect the day
                StudentRepository.currentStudent!!.selectedDays[positionOfSelectedDay(
                    StudentRepository.currentStudent!!
                )] =
                    Day()
            }
        }

    }

}