package com.shkolla.mungesa.repos

import android.content.Context
import androidx.preference.PreferenceManager
import com.shkolla.mungesa.R
import com.shkolla.mungesa.models.Student
import com.shkolla.mungesa.ui.activities.SettingsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection

object StudentRepo {
    // store the list of students
    var students: MutableList<Student> = mutableListOf()
    var absenceUrl = ""

    suspend fun initStudents(c: Context) {
        absenceUrl = PreferenceManager.getDefaultSharedPreferences(c)
            .getString(SettingsActivity.ABSENCE_SMS_LINK, c.getString(R.string.def_absence_link)).toString()
        networkRequest()
    }

    // functions for performing network request

    private suspend fun networkRequest() = withContext(Dispatchers.IO) {

        val url = async { setUpConnection() }
        retrieveData(url.await())

    }

    private fun setUpConnection(): URL? {
        var mUrl: URL? = null

        students.clear()
        try {
            mUrl =
                URL(absenceUrl)
        } catch (e: MalformedURLException) {
            println("Debug: Bad URL!")
        }

        return mUrl
    }

    private fun retrieveData(url: URL?) {
        var tokens: List<String>
        var student: Student?

        try {
            assert(url != null)
            val connection: URLConnection = url!!.openConnection()
            connection.connect()


            val br = BufferedReader(InputStreamReader(connection.getInputStream()))

            br.readLine()

            var line = br.readLine()
            while (line != null) {
                tokens = line.split(",")
                if (tokens.isNotEmpty()) {
                    student = Student(tokens[0], tokens[1], tokens[2])
                    students.add(student)
                    line = br.readLine()
                }
            }

            br.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}