package com.shkolla.mungesa.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shkolla.mungesa.R
import kotlinx.android.synthetic.main.activity_student_profile.*

class StudentProfile : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_profile)

        student_fullname.text = "Emri i plotë: ".plus(intent.getStringExtra("full_name"))
        student_phone_number.text = "Nr. cel: ".plus(intent.getStringExtra("phone_number"))

    }
}
