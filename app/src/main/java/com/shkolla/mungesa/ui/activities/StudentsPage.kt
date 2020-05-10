package com.shkolla.mungesa.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.meet.quicktoast.Quicktoast
import com.shkolla.mungesa.R
import com.shkolla.mungesa.databinding.StudentsPageBinding
import com.shkolla.mungesa.models.Student
import com.shkolla.mungesa.repos.AppData.BACK_PRESS_MESSAGE_PURPOSE
import com.shkolla.mungesa.repos.AppData.SELECT_STUDENT_REQUEST
import com.shkolla.mungesa.repos.AppData.SEND_MESSAGE_PURPOSE
import com.shkolla.mungesa.ui.adapters.StudentAdapter
import com.shkolla.mungesa.ui.dialogs.BasicDialog
import com.shkolla.mungesa.utils.MessageHandler
import com.shkolla.mungesa.viewmodels.StudentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudentsPage : AppCompatActivity(), StudentAdapter.StudentInteraction,
    BasicDialog.DialogButtonInteraction {

    private lateinit var studentViewModel: StudentViewModel
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var binder: StudentsPageBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = DataBindingUtil.setContentView(this, R.layout.students_page)

        initRecyclerView()

        studentViewModel = ViewModelProvider(this).get(StudentViewModel::class.java)
        studentViewModel.initStudents()
        studentViewModel.getStudents().observe(this, Observer {
            studentAdapter.submitList(it)
        })
        studentViewModel.isLoading().observe(this, Observer {
            binder.isLoading = it
        })
    }


    private fun initRecyclerView() {
        studentAdapter = StudentAdapter(this)
        binder.studentRv.adapter = studentAdapter
    }

    private fun sendMessagesDialog() {
        if (studentViewModel.areStudentsSelected()) {
            val content = resources.getString(R.string.askForSendingMessages)

            val dialog = BasicDialog(SEND_MESSAGE_PURPOSE, content, this)

            dialog.show(supportFragmentManager, "sendMessageDialog")

        } else {
            Quicktoast(this).swarn("Zgjidhni të paktën një nxënës")
        }
    }


    override fun onStudentSelected(student: Student) {
        val bundle = bundleOf("currentStudent" to student)
        val i = Intent(this, DaysPage::class.java)
        i.putExtras(bundle)
        startActivityForResult(i, SELECT_STUDENT_REQUEST)
    }

    private fun hasMessagePermission(): Boolean {

        return (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_STUDENT_REQUEST && resultCode == Activity.RESULT_OK) {
            // Select this student

            val currentStudent = data?.extras?.getParcelable<Student>("currentStudent")

            studentViewModel.selectStudent(currentStudent!!)


        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.students_page_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.send -> {
                if (hasMessagePermission()) {
                    studentViewModel.getStudents().value
                    sendMessagesDialog()

                } else {
                    // Request permission
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.SEND_SMS),
                        0
                    )
                }

                true
            }
            R.id.refresh_list -> {
                studentViewModel.initStudents()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onBackPressed() {
        if (!studentViewModel.getStudents().value.isNullOrEmpty() && studentViewModel.areStudentsSelected()) {
            val content = "Përzgjedhjet do të hiqen. Vazhdoni?"
            val dialog =
                BasicDialog(BACK_PRESS_MESSAGE_PURPOSE, content, this)
            dialog.show(supportFragmentManager, "")
        } else {
            super.onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            0 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted. Send messages

                    sendMessagesDialog()
                } else {
                    // Permission Denied
                    Quicktoast(this).swarn("Programi duhet të ketë leje për të dërguar mesazhe")
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onButtonClicked(purpose: Int, button: String) {

        when (purpose) {
            SEND_MESSAGE_PURPOSE -> {
                if (button == "yes") {
                    // send messages

                    GlobalScope.launch {
                        val res = studentViewModel.getStudents().value?.let {
                            MessageHandler.sendMessages(it)
                        }
                        res?.join()
                        studentViewModel.resetStudents()
                        withContext(Dispatchers.Main) {
                            Quicktoast(this@StudentsPage).sinfo("Mesazhet u dërguan")
                        }
                    }

                }
            }

            BACK_PRESS_MESSAGE_PURPOSE -> {
                if (button == "yes") {
                    studentViewModel.resetStudents()
                    super.onBackPressed()
                }
            }
            else -> {
                Quicktoast(this).swarn("Ndodhi një gabim")
            }
        }
    }


}