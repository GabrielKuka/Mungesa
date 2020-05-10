package com.shkolla.mungesa.repos

import android.content.Context
import androidx.preference.PreferenceManager
import com.meet.quicktoast.Quicktoast
import com.shkolla.mungesa.R
import com.shkolla.mungesa.models.BulkMessage
import com.shkolla.mungesa.ui.activities.SettingsActivity
import com.shkolla.mungesa.utils.INetworkCall
import com.shkolla.mungesa.utils.NetworkCall
import kotlinx.coroutines.*

class BulkMessageRepo(private val networkCall: NetworkCall) : INetworkCall by networkCall {
    companion object {
        val bulkMessages: MutableList<BulkMessage> = mutableListOf()
        var bulkSmsUrl = ""
    }

    suspend fun initBulkMessages(c: Context) {

        // 1. Get the url stored in the device
        bulkSmsUrl = PreferenceManager.getDefaultSharedPreferences(c).getString(
            SettingsActivity.BULK_SMS_LINK, c.getString(
                R.string.def_bulk_sms_link
            )
        ).toString()

        // 2. Clear the list if not empty
        bulkMessages.clear()

        // 3. Make the network request
        networkRequest(c)
    }


    private suspend inline fun networkRequest(c: Context) = withContext(Dispatchers.IO) {

        // 1. Check if the url is valid
        val result =
            async {
                networkCall.checkUrl(bulkSmsUrl, c)
            }


        // 2. Retrieve the data from the server
        networkCall.retrieveData(result.await()) { tokens ->
            bulkMessages.add(BulkMessage(tokens[0], tokens[1], tokens[2]))
        }
    }

}