package com.shkolla.mungesa.repos


import com.shkolla.mungesa.models.Day


object DayRepo {

    private val daysList: MutableList<Day> = mutableListOf()

    fun getDays(): MutableList<Day> {
        if (daysList.isNullOrEmpty()) {
            setDays()
        }
        return daysList
    }

    private fun setDays() {
        daysList.add(Day("E Hënë"))
        daysList.add(Day("E Martë"))
        daysList.add(Day("E Mërkurë"))
        daysList.add(Day("E Enjte"))
        daysList.add(Day("E Premte"))

    }

}