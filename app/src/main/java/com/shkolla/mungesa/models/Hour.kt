package com.shkolla.mungesa.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hour(val name: String, var isChecked: Boolean = false) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other !is Hour) {
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