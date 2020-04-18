package com.shkolla.mungesa.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shkolla.mungesa.R
import com.shkolla.mungesa.helpers.AppData
import kotlinx.android.synthetic.main.update_links.*

class UpdateLinks : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_links)

        loadFields()

        update_button.setOnClickListener {
            // Update links
            AppData.downloadUrl = absence_field.text.toString()
            AppData.bulkSmsUrl = bulksms_field.text.toString()
            AppData.displayMessage(this, "Linket e përditësuan.")
        }

    }

    private fun loadFields(){
        absence_field.setText(AppData.downloadUrl)
        bulksms_field.setText(AppData.bulkSmsUrl)
    }

}
