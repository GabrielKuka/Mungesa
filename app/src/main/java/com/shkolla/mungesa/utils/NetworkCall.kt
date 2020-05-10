package com.shkolla.mungesa.utils

import android.content.Context
import com.meet.quicktoast.Quicktoast
import com.shkolla.mungesa.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection

class NetworkCall : INetworkCall {


    override fun checkUrl(url: String, c: Context): URL? {

        return try {

            URL(url)

        } catch (e: MalformedURLException) {

            CoroutineScope(Dispatchers.Main).launch {
                Quicktoast(c).lwarn(c.resources.getString(R.string.invalid_link))
            }

            null
        }

    }


    override fun retrieveData(url: URL?, addItem: (tokens: List<String>) -> Unit) {
        var tokens: List<String>

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
                    addItem(tokens)
                    line = br.readLine()
                }

            }

            br.close()

        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

interface INetworkCall {

    fun checkUrl(url: String, c: Context): URL?

    fun retrieveData(url: URL?, addItem: (tokens: List<String>) -> Unit)
}

