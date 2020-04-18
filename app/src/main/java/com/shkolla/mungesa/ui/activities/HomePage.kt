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
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.shkolla.mungesa.R
import com.shkolla.mungesa.helpers.AppData
import com.shkolla.mungesa.helpers.MessageHandler
import com.shkolla.mungesa.helpers.SelectMode
import com.shkolla.mungesa.interfaces.SelectedStudentListener
import com.shkolla.mungesa.ui.adapters.StudentListAdapter
import com.shkolla.mungesa.ui.dialogs.BasicDialog
import com.shkolla.mungesa.viewmodels.HomepageViewModel
import kotlinx.android.synthetic.main.activity_homepage.*


class HomePage : AppCompatActivity(), SelectedStudentListener {

    private lateinit var hpViewModel: HomepageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        hpViewModel = ViewModelProviders.of(this).get(HomepageViewModel::class.java)

        hpViewModel.refreshStudentList(this)

        SelectMode.disable(this)


    }

    override fun onResume() {
        super.onResume()
        hpViewModel.checkForSelectedDays()
        student_recycler_view.adapter?.notifyDataSetChanged()

    }

    fun initRecyclerView() {
        student_recycler_view.layoutManager = LinearLayoutManager(this)
        student_recycler_view.adapter = StudentListAdapter(this, hpViewModel)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.homepage_menu, menu)
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            0 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    MessageHandler.sendMessages()
                    hpViewModel.resetLists()
                } else {
                    // permission denied!
                    AppData.displayMessage(
                        this,
                        "Ky program duhet të ketë leje për të dërguar mesazhe."
                    )
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun hasMessagePermission(): Boolean {

        return (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.bulksms -> {
                SelectMode.disable(this)
                startActivity(Intent(this, BulkSms::class.java))
                true
            }
            R.id.send -> {
                // send messages
                if (SelectMode.isEnabled()) {
                    if (hasMessagePermission()) {
                        // Permission is granted
                        val content = resources.getString(R.string.sendMessageDialogContent)
                        val dialog = BasicDialog(content) { MessageHandler.sendMessages() }

                        dialog.show(supportFragmentManager, "")

                        hpViewModel.resetLists()
                    } else {
                        // Request permission
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.SEND_SMS),
                            0
                        )
                    }

                } else {
                    AppData.displayMessage(this, "Zgjidhni nxënës më parë.")
                }
                true
            }
            R.id.select -> {
                // enable select mode to select students
                if (SelectMode.isEnabled())
                    hpViewModel.resetLists()
                SelectMode.enable(this)
                true
            }
            R.id.refresh -> {

                // disable select mode
                SelectMode.disable(this)

                // Refresh student list from csv file and update database
                hpViewModel.refreshStudentList(this)

                true
            }
            R.id.update_links -> {
                startActivity(Intent(this, UpdateLinks::class.java))
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onBackPressed() {


        if (SelectMode.isEnabled()) {
            // disable select mode if on
            SelectMode.disable(this)

            // empty selected students
            hpViewModel.getSelectedStudents().clear()

        } else {
            super.onBackPressed()
        }
    }

    override fun onSelectedStudent(position: Int) {
        if (SelectMode.isEnabled()) {

            hpViewModel.setCurrentStudent(hpViewModel.getStudent(position))


            startActivity(Intent(this, DaysAndHours::class.java))
            hpViewModel.selectStudent(hpViewModel.getStudent(position))


        } else {
            // Open student's profile
            val i = Intent(this, StudentProfile::class.java)
            i.putExtra(
                "full_name",
                hpViewModel.getStudent(position).firstName + " " + hpViewModel.getStudent(position).lastName
            )
            i.putExtra("phone_number", hpViewModel.getStudent(position).phoneNumber)
            startActivity(i)
        }
    }


}
