package com.shkolla.mungesa.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

internal object InternetCheck {

    suspend fun checkInternet(): Boolean {

        val res = CoroutineScope(Dispatchers.IO).async {
           isNetAvailable()
        }

        return res.await()
    }

    private fun isNetAvailable(): Boolean {
        return try {
            val sock = Socket()
            sock.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            sock.close()
            true
        } catch (e: IOException) {
            false
        }
    }

}