package com.shkolla.mungesa.utils

import android.telephony.SmsManager
import com.shkolla.mungesa.models.BulkMessage
import com.shkolla.mungesa.models.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object MessageHandler {


    private val smsManager = SmsManager.getDefault()

    suspend fun sendMessages(students: List<Student>) = CoroutineScope(Dispatchers.Default).launch {

        for (student in students) {
            if (student.isChecked) {
                val msg =  setUpMessagePerStudent(student)
                sendMessagePerStudent(student.phoneNumber, msg)
            }
        }

    }


    suspend fun sendBulkMessages(bulkMessages: List<BulkMessage>): Boolean {
        var status = false
        val job = CoroutineScope(Dispatchers.Default).launch {
            for (item in bulkMessages) {
                sendMessagePerStudent(item.phoneNumber, item.message)
                // println(item.phoneNumber + ": " + item.message)
            }
            status = true
        }
        job.join()

        return status
    }

    private fun setUpMessagePerStudent(student: Student): String {

        val builder = StringBuilder()

        builder.append("I nderuar prind, nxënësi ")
            .append(student.firstName)
            .append(" ")
            .append(student.lastName)
            .append(" ka munguar")

        for (day in student.days) {
            if (day.isChecked) {
                builder
                    .append("\n")
                    .append(" ditën ")
                    .append(day.name.toLowerCase())

                for (hour in day.hours) {
                    if (hour.isChecked) {
                        builder
                            .append(" orën ")
                            .append(hour.name.toLowerCase())
                            .append(",")
                    }
                }
            }

        }

        return builder.toString()
    }

    private fun sendMessagePerStudent(phoneNumber: String, msg: String) {
        val messageArray = smsManager.divideMessage(msg)
        smsManager.sendMultipartTextMessage(phoneNumber, null, messageArray, null, null)
      //  smsManager.sendTextMessage(phoneNumber, null, msg, null, null)
    }

}