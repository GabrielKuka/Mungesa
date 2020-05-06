package com.shkolla.mungesa.models

import android.os.Parcelable
import com.shkolla.mungesa.repos.HourRepo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Day(
    val name: String,
    var isChecked: Boolean = false,
    var isCurrent: Boolean = false,
    var hours: MutableList<Hour> = HourRepo.getHours()
) :
    Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other !is Day) {
            return false
        }

        if (other.isChecked != isChecked) {
            return false
        }

        if (other.name != name) {
            return false
        }

        return true
    }

}