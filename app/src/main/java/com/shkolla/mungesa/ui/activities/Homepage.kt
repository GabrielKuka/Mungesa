package com.shkolla.mungesa.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.shkolla.mungesa.R
import com.shkolla.mungesa.viewmodels.StudentViewModel

class Homepage : AppCompatActivity() {

    private lateinit var studentViewModel: StudentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        studentViewModel = ViewModelProvider(this).get(StudentViewModel::class.java)
    }

    fun getViewModel(): StudentViewModel {
        return studentViewModel
    }
}
