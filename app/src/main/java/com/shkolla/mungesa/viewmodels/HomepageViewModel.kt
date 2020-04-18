package com.shkolla.mungesa.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import com.shkolla.mungesa.background.InternetCheck
import com.shkolla.mungesa.helpers.AppData
import com.shkolla.mungesa.models.Student
import com.shkolla.mungesa.repositories.DaysAndHoursRepository
import com.shkolla.mungesa.repositories.StudentRepository
import com.shkolla.mungesa.ui.activities.HomePage
import kotlinx.android.synthetic.main.activity_homepage.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

class HomepageViewModel : ViewModel() {


    fun refreshStudentList(ac: HomePage) {
        // Check if the user is connected to the internet
        InternetCheck { internet ->
            if (internet)
               networkRequest(ac)
            else
                AppData.displayMessage(ac, "Pajisja nuk është lidhur me internet!")

        }

        resetLists()

    }

    private fun networkRequest(ac: HomePage) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = StudentRepository.setUpConnection()
            withContext(Dispatchers.Default) {
                StudentRepository.retrieveData(result)
            }

            withContext(Dispatchers.Main){
                ac.initRecyclerView()
                ac.progress_bar.visibility = View.GONE
            }
        }
    }


    fun resetLists() {
        CoroutineScope(Dispatchers.Default).launch {
            StudentRepository.selectedStudents = ArrayList()
            StudentRepository.currentStudent = null
            DaysAndHoursRepository.currentDay = null
        }

    }


    fun getStudent(position: Int): Student = StudentRepository.studentList[position]

    fun setCurrentStudent(student: Student?) {
        StudentRepository.currentStudent = student
    }

    private fun getCurrentStudent() = StudentRepository.currentStudent

    fun getSelectedStudents(): MutableList<Student> = StudentRepository.selectedStudents

    fun getStudents(): MutableList<Student> = StudentRepository.studentList


    fun isStudentSelected(student: Student): Boolean {
        var isSelected = false

        for (st in StudentRepository.selectedStudents) {
            if (student == st) {
                isSelected = true
                break
            }
        }

        return isSelected
    }


    fun selectStudent(student: Student) {
        CoroutineScope(Dispatchers.Default).launch {
            if (!isStudentSelected(student)) {
                getSelectedStudents().add(student)
            }
        }
    }

    private fun getNumberOfSelectedDays(student: Student?): Int {
        var counter = 0

        for (day in student?.selectedDays!!) {
            if (day.name != "") {
                counter++
            }
        }
        return counter

    }

    fun checkForSelectedDays() {
        CoroutineScope(Dispatchers.Default).launch {
            // If there are no days selected
            if (getCurrentStudent() != null && getNumberOfSelectedDays(StudentRepository.currentStudent) == 0) {
                // remove selected student
                getSelectedStudents().remove(StudentRepository.currentStudent!!)
            }
            // reset current student
            DaysAndHoursRepository.currentDay = null
            setCurrentStudent(null)
        }


    }


}