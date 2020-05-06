package com.shkolla.mungesa.models

import android.os.Parcelable
import com.shkolla.mungesa.repos.DayRepo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Student(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    var days: MutableList<Day> = DayRepo.getDays(),
    var isChecked: Boolean = false,
    var isCurrent: Boolean = false
) : Parcelable {


}

