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
import com.shkolla.mungesa.utils.ExcelFileReader
import com.shkolla.mungesa.utils.IFilePath
import kotlinx.android.synthetic.main.home_page_activity.*
import java.io.File

class HomePage : AppCompatActivity(), IFilePath {

    private lateinit var pathPref: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page_activity)

        requestPermission()

    }

    override fun onStart() {
        super.onStart()

        pathPref = PreferenceManager.getDefaultSharedPreferences(this)
            .getString(SettingsActivity.EXCEL_FILE_KEY, "").toString()

        absence_button.setOnClickListener {

            if (checkExcelFile(pathPref)) {
                startActivity(Intent(this, StudentsPage::class.java))
            }

        }

        bulk_sms_button.setOnClickListener {
            if (checkExcelFile(pathPref)) {
                startActivity(Intent(this, BulkSms::class.java))
            }

        }
    }

    private fun checkExcelFile(path: String): Boolean {
        val fileReader = ExcelFileReader(this)

        if (!fileReader.validatePath(path)) return false

        if (!fileReader.validateFile(File(path))) return false

        return true
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
            R.id.about_app -> {
                startActivity(Intent(this, AboutTheApp::class.java))
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onWrongNumberOfSheets() {
        TextDialog(resources.getString(R.string.invalid_no_of_sheets)).show(
            supportFragmentManager,
            ""
        )
    }

    override fun onInvalidPath() {
        TextDialog(resources.getString(R.string.invalid_path)).show(supportFragmentManager, "")
    }

    override fun onInvalidFile() {
        TextDialog(resources.getString(R.string.invalid_file)).show(supportFragmentManager, "")
    }

    override fun onFileMissing() {
        TextDialog(resources.getString(R.string.no_file)).show(supportFragmentManager, "")
    }

}
