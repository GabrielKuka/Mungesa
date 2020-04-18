package com.shkolla.mungesa.ui.activities

import android.Manifest
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
import com.shkolla.mungesa.models.BulkMessage
import com.shkolla.mungesa.ui.adapters.BulkSmsAdapter
import com.shkolla.mungesa.ui.dialogs.BasicDialog
import com.shkolla.mungesa.viewmodels.BulkSmsViewModel
import kotlinx.android.synthetic.main.activity_bulk_sms.*

class BulkSms : AppCompatActivity(), BulkSmsAdapter.Interaction {

    lateinit var bulkSmsAdapter: BulkSmsAdapter
    lateinit var bulkSmsViewModel: BulkSmsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bulk_sms)

        bulkSmsViewModel = ViewModelProviders.of(this).get(BulkSmsViewModel::class.java)

        initRecyclerView()
        bulkSmsViewModel.retrieveList(this)
    }

    private fun initRecyclerView() {
        bulksms_rv.apply {
            layoutManager = LinearLayoutManager(context)
            bulkSmsAdapter = BulkSmsAdapter(this@BulkSms)
            adapter = bulkSmsAdapter
        }
    }

    override fun onItemSelected(position: Int, item: BulkMessage) {
        AppData.displayMessage(this, item.fullName)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bulksms_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.send_bulksms -> {
                // check permissions
                if (hasMessagePermission()) {
                    // send messages

                    val content = resources.getString(R.string.sendMessageDialogContent)
                    val dialog = BasicDialog(content) {
                        MessageHandler.sendBulkMessages()
                    }

                    dialog.show(supportFragmentManager, "")

                } else {
                    // request permission
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.SEND_SMS),
                        0
                    )
                }

                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            0 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay!
                    MessageHandler.sendBulkMessages()
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

}
