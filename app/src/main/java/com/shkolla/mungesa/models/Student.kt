package com.shkolla.mungesa.models

data class Student(var firstName: String, var lastName: String, var phoneNumber: String,
                   var selectedDays: MutableList<Day> = mutableListOf(Day(), Day(), Day(), Day(), Day())
)