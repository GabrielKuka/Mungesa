package com.shkolla.mungesa.ui.activities


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shkolla.mungesa.R


class AboutTheApp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_page)

        title = resources.getString(R.string.about_me)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }


}
