package com.shkolla.mungesa.repos

import android.content.Context
import androidx.preference.PreferenceManager
import com.shkolla.mungesa.R
import com.shkolla.mungesa.models.BulkMessage
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

object BulkMessageRepo {
    val bulkMessages: MutableList<BulkMessage> = mutableListOf()
    var bulkSmsUrl = ""

    suspend fun initBulkMessages(c: Context) {
        bulkSmsUrl = PreferenceManager.getDefaultSharedPreferences(c).getString(
            SettingsActivity.BULK_SMS_LINK, c.getString(
                R.string.def_bulk_sms_link
            )
        ).toString()
        networkRequest()
    }


    private suspend fun networkRequest() = withContext(Dispatchers.IO) {
        val result = async { setUpConnection() }
        retrieveBulkMessages(result.await())
    }

    private fun setUpConnection(): URL? {
        var mUrl: URL? = null

        bulkMessages.clear()
        try {
            mUrl =
                URL(bulkSmsUrl)
        } catch (e: MalformedURLException) {
            println("Debug: Bad URL!")
        }

        return mUrl
    }

    private fun retrieveBulkMessages(url: URL?) {
        var tokens: List<String>
        var bulkMessage: BulkMessage?

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
                    bulkMessage = BulkMessage(tokens[0], tokens[1], tokens[2])
                    bulkMessages.add(bulkMessage)
                    line = br.readLine()
                }
            }

            br.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}