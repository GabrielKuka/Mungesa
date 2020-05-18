package com.shkolla.mungesa.repos

import android.content.Context
import androidx.preference.PreferenceManager
import com.shkolla.mungesa.R
import com.shkolla.mungesa.models.Student
import com.shkolla.mungesa.ui.activities.SettingsActivity
import com.shkolla.mungesa.utils.INetworkCall
import com.shkolla.mungesa.utils.NetworkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class StudentRepo(private val networkCall: NetworkCall) : INetworkCall by networkCall {

    companion object {
        var students: MutableList<Student> = mutableListOf()
        var absenceUrl = ""
    }

    suspend fun initStudents(c: Context) {

        // 1. Get the url stored in the device
        absenceUrl = PreferenceManager.getDefaultSharedPreferences(c)
            .getString(SettingsActivity.ABSENCE_SMS_LINK, c.getString(R.string.def_absence_link))
            .toString()

        // 2. Clear the list if not empty
        students.clear()

        // 3. Make the network request
        networkRequest(c)
    }


    private suspend inline fun networkRequest(c: Context) = withContext(Dispatchers.IO) {

        // 1. Check if url is valid
        val url = async {
            networkCall.checkUrl(absenceUrl, c)
        }

        // 2. Retrieve the data
        retrieveData(url.await()) {
            students.add(Student(it[0], it[1], it[2]))
        }

    }


}