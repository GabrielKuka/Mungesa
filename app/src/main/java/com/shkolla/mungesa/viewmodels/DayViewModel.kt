package com.shkolla.mungesa.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shkolla.mungesa.models.Day
import com.shkolla.mungesa.models.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DayViewModel : ViewModel() {
    private val _days: MutableLiveData<MutableList<Day>> = MutableLiveData()

    fun initDays(currentStudent: Student) {
        _days.value =  currentStudent.days
    }

    fun getDays(): LiveData<MutableList<Day>> {
        return _days
    }

    fun selectDay(day: Day) = viewModelScope.launch(Dispatchers.Default) {

        val result = async { areHoursSelected(day) }

        _days.value?.find { d -> d.name == day.name }?.apply {
            isChecked = result.await()
            hours = day.hours
        }

        day.isChecked = result.await()

        currentDay(day)
        withContext(Dispatchers.Main) {
            _days.notifyObserver()
        }
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