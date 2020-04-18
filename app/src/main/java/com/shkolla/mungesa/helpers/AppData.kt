package com.shkolla.mungesa.helpers

import android.app.Application
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shkolla.mungesa.models.Day
import com.shkolla.mungesa.models.Student


class AppData : Application() {
    companion object {
        const val fileName = "data.csv"
         var bulkSmsUrl = "https://drive.google.com/u/0/uc?id=1kZInKVLiM_Cts7sXfVaLGl8x8eOvaEB8&export=download"
         var downloadUrl = "https://drive.google.com/u/0/uc?id=19nloeM5lIkIvAwhWh4wO6Gb2R9sBNBeC&export=download"
        const val downloadDirectory = "Mungesat"

        var selectMode = false

        val displayMessage: (AppCompatActivity, String) ->  Unit = {a, m -> Toast.makeText(a, m, Toast.LENGTH_SHORT).show() }





    }

}