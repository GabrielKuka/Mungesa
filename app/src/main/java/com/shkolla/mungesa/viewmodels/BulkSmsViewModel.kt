package com.shkolla.mungesa.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shkolla.mungesa.models.BulkMessage
import com.shkolla.mungesa.repos.BulkMessageRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BulkSmsViewModel(application: Application) : AndroidViewModel(application) {
    private val _bulkMessages: MutableLiveData<MutableList<BulkMessage>> = MutableLiveData()
    private val _isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }

    fun initBulkMessages() = viewModelScope.launch {

        if (!_isLoading.value!!) {        // Makes sure it doesn't reload while it is already reloading
            _isLoading.value = true

            CoroutineScope(Dispatchers.IO).launch {
                BulkMessageRepo().initBulkMessages(getApplication())
                withContext(Dispatchers.Main) {
                    _bulkMessages.value = BulkMessageRepo.bulkMessages
                    _isLoading.value = false
                }
            }


        }

    }

    fun isLoading(): LiveData<Boolean> {
        return _isLoading
    }

    fun getBulkMessages(): LiveData<MutableList<BulkMessage>> {
        return _bulkMessages
    }
}