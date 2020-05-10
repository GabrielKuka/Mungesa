package com.shkolla.mungesa.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shkolla.mungesa.models.Day
import com.shkolla.mungesa.models.Student

class DayViewModel : ViewModel() {
    private val _days: MutableLiveData<MutableList<Day>> = MutableLiveData()

    fun initDays(currentStudent: Student) {
        _days.value = currentStudent.days
    }

    fun getDays(): LiveData<MutableList<Day>> {
        return _days
    }

    fun selectDay(day: Day) {
        day.isChecked = areHoursSelected(day)

        _days.value?.find { d -> d.name == day.name }?.apply {
            isChecked = day.isChecked
            hours = day.hours
        }

        currentDay(day)
        _days.notifyObserver()
    }

    fun currentDay(day: Day) {
        day.isCurrent = !day.isCurrent
    }

    private fun areHoursSelected(day: Day): Boolean {
        for (h in day.hours) {
            if (h.isChecked) {
                return true
            }
        }

        return false
    }

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}