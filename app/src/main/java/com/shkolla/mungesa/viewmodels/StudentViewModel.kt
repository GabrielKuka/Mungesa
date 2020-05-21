package com.shkolla.mungesa.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shkolla.mungesa.models.Day
import com.shkolla.mungesa.models.Student
import com.shkolla.mungesa.repos.StudentRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val _isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }

    private val _students: MutableLiveData<MutableList<Student>> =
        MutableLiveData()


    fun isLoading(): LiveData<Boolean> = _isLoading

    fun getStudents(): LiveData<MutableList<Student>> = _students


    fun initStudents() = viewModelScope.launch {
        if (!_isLoading.value!!) {
            _isLoading.value = true

            CoroutineScope(Dispatchers.IO).launch {
                StudentRepo().initStudents(getApplication())
                withContext(Dispatchers.Main) {
                    _students.value = StudentRepo.students
                    _isLoading.value = false
                }
            }

        }

    }

    private fun currentStudent(currentStudent: Student) {
        currentStudent.isCurrent = !currentStudent.isCurrent
    }

    fun selectStudent(currentStudent: Student) = viewModelScope.launch {
        withContext(Dispatchers.Default) {
            currentStudent.isChecked = areDaysSelected(currentStudent.days)

            _students.value?.find { s -> s.phoneNumber == currentStudent.phoneNumber }?.apply {
                isChecked = currentStudent.isChecked
                days = currentStudent.days
            }

            currentStudent(currentStudent)
            withContext(Dispatchers.Main) {
                _students.notifyObserver()
            }
        }

    }

    fun areStudentsSelected(): Boolean {
        for (st in _students.value!!.iterator()) {
            if (st.isChecked) {
                return true
            }
        }
        return false
    }

    private fun areDaysSelected(days: List<Day>): Boolean {
        for (day in days) {
            if (day.isChecked) {
                return true
            }
        }

        return false
    }

    fun resetStudents() = viewModelScope.launch {
        withContext(Dispatchers.Default) {
            StudentRepo.students.forEach { st ->
                if (st.isChecked) {
                    st.days.forEach { d ->
                        if (d.isChecked) {
                            d.hours.forEach { h ->
                                if (h.isChecked) {
                                    h.isChecked = false
                                }
                            }
                            d.isChecked = false
                        }
                    }
                    st.isChecked = false
                }
            }
        }

        _students.notifyObserver()
    }

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }

}