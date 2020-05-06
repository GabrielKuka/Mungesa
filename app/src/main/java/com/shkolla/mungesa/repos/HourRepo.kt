package com.shkolla.mungesa.repos


import com.shkolla.mungesa.models.Hour

object HourRepo {

    private val hoursList: MutableList<Hour> = arrayListOf()

    fun getHours(): MutableList<Hour> {
        if (hoursList.isNullOrEmpty()) {
            setHours()
        }
        return hoursList
    }

    private fun setHours() {


        hoursList.add(Hour("E Parë"))
        hoursList.add(Hour("E Dytë"))
        hoursList.add(Hour("E Tretë"))
        hoursList.add(Hour("E Katërt"))
        hoursList.add(Hour("E Pestë"))
        hoursList.add(Hour("E Gjashtë"))
        hoursList.add(Hour("E Shtatë"))

    }
}