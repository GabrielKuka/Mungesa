package com.shkolla.mungesa.repos

import com.shkolla.mungesa.models.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection

object StudentRepo {
    // store the list of students
    var students: MutableList<Student> = mutableListOf()

    init {
        networkRequest()
    }

    // functions for performing network request

    private fun networkRequest() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = setUpConnection()
            retrieveData(result)
        }
    }

    private fun setUpConnection(): URL? {
        var mUrl: URL? = null
        students.clear()
        try {
            mUrl =
                URL(AppData.downloadUrl)
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