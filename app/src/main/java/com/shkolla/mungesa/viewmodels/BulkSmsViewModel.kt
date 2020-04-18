package com.shkolla.mungesa.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import com.shkolla.mungesa.background.InternetCheck
import com.shkolla.mungesa.helpers.AppData
import com.shkolla.mungesa.models.BulkMessage
import com.shkolla.mungesa.repositories.BulkSmsRepository
import com.shkolla.mungesa.ui.activities.BulkSms
import kotlinx.android.synthetic.main.activity_bulk_sms.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BulkSmsViewModel: ViewModel() {

    private fun getSmsList(): ArrayList<BulkMessage> {
        return BulkSmsRepository.list
    }

    private fun clearSmsList(){
        getSmsList().clear()
    }

    private fun networkRequest(ac: BulkSms){
        CoroutineScope(Dispatchers.IO).launch {
            clearSmsList()
            val result = BulkSmsRepository.setUpConnection()
            withContext(Dispatchers.Default){
                BulkSmsRepository.retrieveData(result)
            }
            withContext(Dispatchers.Main){
                ac.bulkSmsAdapter.submitList(getSmsList())
                ac.progressBar.visibility = View.GONE
            }
        }
    }

    fun retrieveList(ac: BulkSms) {
        InternetCheck {internet ->
            if(internet)
                networkRequest(ac)
            else
                AppData.displayMessage(ac, "Pajisja nuk është lidhur me internet!")
        }

        clearSmsList()
    }

}