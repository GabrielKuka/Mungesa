package com.shkolla.mungesa.repos

import com.shkolla.mungesa.models.Day
import com.shkolla.mungesa.models.Hour
import com.shkolla.mungesa.models.Student

object AppData {

    var bulkSmsUrl =
        "https://drive.google.com/u/0/uc?id=1kZInKVLiM_Cts7sXfVaLGl8x8eOvaEB8&export=download"
    var downloadUrl =
        "https://drive.google.com/u/0/uc?id=19nloeM5lIkIvAwhWh4wO6Gb2R9sBNBeC&export=download"

    val selectedStudents: HashMap<Student, HashMap<Day, MutableList<Hour>>> = HashMap()

    val tempDays: MutableList<Day> = mutableListOf(

        Day("E Hënë"),
        Day("E Martë"),
        Day("E Mërkurë"),
        Day("E Enjte"),
        Day("E Premte")
    )

    val tempHours: MutableList<Hour> = mutableListOf(
        Hour("E Parë"),
        Hour("E Dytë"),
        Hour("E Tretë"),
        Hour("E Katërt"),
        Hour("E Pestë"),
        Hour("E Gjashtë"),
        Hour("E Shtatë")
    )

    fun resetHours() {
        tempHours.clear()
        tempHours.add(Hour("E Parë"))
        tempHours.add(Hour("E Dytë"))
        tempHours.add(Hour("E Tretë"))
        tempHours.add(Hour("E Katërt"))
        tempHours.add(Hour("E Pestë"))
        tempHours.add(Hour("E Gjashtë"))
        tempHours.add(Hour("E Shtatë"))
    }

    fun resetDays() {
        tempDays.clear()
        tempDays.add(Day("E Hënë"))
        tempDays.add(Day("E Martë"))
        tempDays.add(Day("E Mërkurë"))
        tempDays.add(Day("E Enjte"))
        tempDays.add(Day("E Premte"))
    }
}