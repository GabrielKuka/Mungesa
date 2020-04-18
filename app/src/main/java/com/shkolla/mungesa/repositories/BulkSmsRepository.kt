package com.shkolla.mungesa.repositories

import com.shkolla.mungesa.helpers.AppData
import com.shkolla.mungesa.models.BulkMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection

object BulkSmsRepository {

    val list = ArrayList<BulkMessage>()


     fun setUpConnection(): URL? {
        var mUrl: URL? = null
        try {
            mUrl =
                URL(AppData.bulkSmsUrl)
        } catch (e: MalformedURLException) {
            println("Debug: Bad URL!")
        }
        return mUrl
    }

     fun retrieveData(url: URL?){
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
                    list.add(bulkMessage)

                    line = br.readLine()
                }
            }

            br.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}