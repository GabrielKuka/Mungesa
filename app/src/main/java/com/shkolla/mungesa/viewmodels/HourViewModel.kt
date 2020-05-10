package com.shkolla.mungesa.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shkolla.mungesa.models.Day
import com.shkolla.mungesa.models.Hour

class HourViewModel : ViewModel() {
    private val _hours: MutableLiveData<MutableList<Hour>> = MutableLiveData()

    fun initHours(currentDay: Day) {
        _hours.value = currentDay.hours
    }

    fun getHours(): LiveData<MutableList<Hour>> {
        return _hours
    }

    fun selectHour(hour: Hour) {
        hour.isChecked = !hour.isChecked
        _hours.notifyObserver()
    }

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}