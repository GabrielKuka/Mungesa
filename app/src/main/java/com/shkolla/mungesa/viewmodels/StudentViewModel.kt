package com.shkolla.mungesa.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shkolla.mungesa.models.Day
import com.shkolla.mungesa.models.Hour
import com.shkolla.mungesa.models.Student
import com.shkolla.mungesa.repos.AppData
import com.shkolla.mungesa.repos.StudentRepo

class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val _students: MutableLiveData<MutableList<Student>> =
        MutableLiveData<MutableList<Student>>().apply {
            value = StudentRepo.students
        }

    private val _days: MutableLiveData<MutableList<Day>> = MutableLiveData()

    private val _hours: MutableLiveData<MutableList<Hour>> = MutableLiveData()

    fun getStudents(): LiveData<MutableList<Student>> {
        return _students
    }

    fun getDays(currentStudent: Student): LiveData<MutableList<Day>> {
        _days.value = currentStudent.days
        return _days
    }

    fun getHours(currentDay: Day): LiveData<MutableList<Hour>> {
        _hours.value = currentDay.hours
        return _hours
    }

    fun currentStudent(currentStudent: Student) {
        currentStudent.isCurrent = !currentStudent.isCurrent
    }

    fun currentDay(currentDay: Day) {
        currentDay.isCurrent = !currentDay.isCurrent
    }

    fun selectHour(currentDay: Day, hour: Hour) {
        hour.isChecked = !hour.isChecked

        _hours.notifyObserver()
    }

    fun addSelectedHours(currentDay: Day) {

        currentDay.isChecked = areHoursSelected(currentDay.hours)

        currentDay(currentDay)


    }

    fun addSelectedDays(currentStudent: Student) {

        currentStudent.isChecked = areDaysSelected(currentStudent.days)

        currentStudent(currentStudent)
    }

    private fun areDaysSelected(days: List<Day>): Boolean {
        for (day in days) {
            if (day.isChecked) {
                return true
            }
        }

        return false
    }


    private fun areHoursSelected(list: List<Hour>): Boolean {
        for (hour in list) {
            if (hour.isChecked) {
                return true
            }
        }

        return false
    }


    private fun resetTempDays() {
        AppData.resetDays()
    }

    private fun getTempDays(): MutableList<Day> {
        return AppData.tempDays
    }

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }

}