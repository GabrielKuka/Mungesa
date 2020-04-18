package com.shkolla.mungesa.repositories

import com.shkolla.mungesa.helpers.AppData
import com.shkolla.mungesa.models.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.*

object StudentRepository {

    val studentList: MutableList<Student> = ArrayList()
    var selectedStudents: MutableList<Student> = ArrayList()
    var currentStudent: Student? = null


    fun setUpConnection(): URL? {
        var mUrl: URL? = null
        studentList.clear()
        try {
            mUrl =
                URL(AppData.downloadUrl)
        } catch (e: MalformedURLException) {
            println("Debug: Bad URL!")
        }

        return mUrl
    }

    fun retrieveData(url: URL?) {
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
                    studentList.add(student)
                    line = br.readLine()
                }
            }

            br.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}