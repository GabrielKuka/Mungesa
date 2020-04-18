package com.shkolla.mungesa.helpers

import android.telephony.SmsManager
import com.shkolla.mungesa.models.BulkMessage
import com.shkolla.mungesa.models.Student
import com.shkolla.mungesa.repositories.BulkSmsRepository
import com.shkolla.mungesa.repositories.StudentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MessageHandler {

    private var messagePerStudent = ""
    private val smsManager = SmsManager.getDefault()

    fun sendBulkMessages() {
        CoroutineScope(Dispatchers.Default).launch {
            for (sms in BulkSmsRepository.list) {
                //          sendMessagePerStudent(sms)
                println(sms.message)
            }
        }
    }

    fun sendMessages() {
        CoroutineScope(Dispatchers.Default).launch {
            for (student in StudentRepository.selectedStudents) {
//                setUpMessagePerStudent(student)
//                sendMessagePerStudent(student)
                println("Student: ${student.firstName}")

            }
        }

    }

    private fun setUpMessagePerStudent(student: Student) {
        val builder = StringBuilder()



        builder.append("I nderuar prind, nxënësi ")
            .append(student.firstName)
            .append(" ")
            .append(student.lastName)

        for (day in student.selectedDays) {
            if (day.name.isNotEmpty())
                builder
                    .append("\n")
                    .append(" ka munguar ditën: ")
                    .append(day.name)
                    .append("\n")
            for (hour in day.selectedHours) {
                if (hour.isNotEmpty())
                    builder
                        .append(" orën ")
                        .append(hour)
                        .append(",")
            }

        }


        messagePerStudent = builder.toString()
    }

    private fun sendMessagePerStudent(student: Student) {
        //send Message here
        smsManager.sendTextMessage(student.phoneNumber, null, messagePerStudent, null, null)
    }

    private fun sendMessagePerStudent(bulkMessage: BulkMessage) {
        smsManager.sendTextMessage(bulkMessage.phoneNumber, null, bulkMessage.message, null, null)
    }

}