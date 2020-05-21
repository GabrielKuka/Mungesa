package com.shkolla.mungesa.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.meet.quicktoast.Quicktoast
import com.shkolla.mungesa.R
import com.shkolla.mungesa.ui.dialogs.TextDialog
import com.shkolla.mungesa.utils.IFilePath
import kotlinx.android.synthetic.main.home_page_activity.*
import java.io.File

class HomePage : AppCompatActivity(), IFilePath {

    private lateinit var pathPref: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page_activity)

        requestPermission()

        pathPref = PreferenceManager.getDefaultSharedPreferences(this)
            .getString(SettingsActivity.EXCEL_FILE_KEY, "").toString()

        absence_button.setOnClickListener {

            if (isPathValid(pathPref)) {
                startActivity(Intent(this, StudentsPage::class.java))
            }

        }

        bulk_sms_button.setOnClickListener {
            if (isPathValid(pathPref)) {
                startActivity(Intent(this, BulkSms::class.java))
            }

        }
    }

    override fun onStart() {
        super.onStart()
        checkExcelFile()
    }

    private fun checkExcelFile() {
        isPathValid(pathPref)
    }

    private fun requestPermission() {
        if (!hasMessagePermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.SEND_SMS),
                1
            )
        }
    }

    private fun hasMessagePermission(): Boolean {

        return (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted. Send messages

                } else {
                    // Permission Denied
                    Quicktoast(this).swarn("Programi duhet të ketë leje për të dërguar mesazhe")
                    finish()
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.homepage_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings_button -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun isPathValid(path: String): Boolean {
        if (path == "") {
            val content = resources.getString(R.string.invalid_path)
            TextDialog(content).show(supportFragmentManager, "")
            return false
        }

        if (!path.endsWith(".xls") && !path.endsWith(".xlsx")) {
            val content = resources.getString(R.string.invalid_file)
            TextDialog(content).show(supportFragmentManager, "")
            return false
        }

        if (!File(path).exists()) {
            val content = resources.getString(R.string.no_file)
            TextDialog(content).show(supportFragmentManager, "")
            return false
        }

        return true
    }
}
