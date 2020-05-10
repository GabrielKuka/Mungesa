package com.shkolla.mungesa.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.meet.quicktoast.Quicktoast
import com.shkolla.mungesa.models.BulkMessage
import com.shkolla.mungesa.repos.BulkMessageRepo
import com.shkolla.mungesa.utils.InternetCheck
import kotlinx.coroutines.launch

class BulkSmsViewModel(application: Application) : AndroidViewModel(application) {
    private val _bulkMessages: MutableLiveData<MutableList<BulkMessage>> = MutableLiveData()
    private val _isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = true }

    fun initBulkMessages() = viewModelScope.launch {
        _isLoading.value = true
        if (InternetCheck.checkInternet()) {

            BulkMessageRepo.initBulkMessages(getApplication())
            _bulkMessages.value = BulkMessageRepo.bulkMessages

        } else {
            Quicktoast(this@BulkSmsViewModel.getApplication()).swarn("Nuk ka qasje në internet")
        }

        _isLoading.value = false

    }

    fun isLoading(): LiveData<Boolean> {
        return _isLoading
    }

    fun getBulkMessages(): LiveData<MutableList<BulkMessage>> {
        return _bulkMessages
    }
}