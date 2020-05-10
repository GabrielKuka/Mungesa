package com.shkolla.mungesa.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.meet.quicktoast.Quicktoast
import com.shkolla.mungesa.R
import com.shkolla.mungesa.databinding.BulkSmsActivityBinding
import com.shkolla.mungesa.models.BulkMessage
import com.shkolla.mungesa.repos.AppData.SEND_MESSAGE_PURPOSE
import com.shkolla.mungesa.ui.adapters.BulkMessageAdapter
import com.shkolla.mungesa.ui.dialogs.BasicDialog
import com.shkolla.mungesa.ui.dialogs.TextDialog
import com.shkolla.mungesa.utils.MessageHandler
import com.shkolla.mungesa.viewmodels.BulkSmsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BulkSms : AppCompatActivity(), BasicDialog.DialogButtonInteraction,
    BulkMessageAdapter.BulkMessageInteraction {

    private lateinit var bulkMessageAdapter: BulkMessageAdapter
    private lateinit var bulkSmsViewModel: BulkSmsViewModel
    private lateinit var binder: BulkSmsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = DataBindingUtil.setContentView(this, R.layout.bulk_sms_activity)

        initRecyclerView()

        bulkSmsViewModel = ViewModelProvider(this).get(BulkSmsViewModel::class.java)
        bulkSmsViewModel.initBulkMessages()
        bulkSmsViewModel.getBulkMessages().observe(this, Observer {
            bulkMessageAdapter.submitList(it)
        })

        bulkSmsViewModel.isLoading().observe(this, Observer {
            binder.isLoading = it
        })

    }

    private fun initRecyclerView() {
        bulkMessageAdapter = BulkMessageAdapter(this)
        binder.bulkSmsRv.adapter = bulkMessageAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bulk_sms_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.send_bulk -> {
                // show dialog
                val content = resources.getString(R.string.askForSendingMessages)
                val dialog = BasicDialog(SEND_MESSAGE_PURPOSE, content, this)
                dialog.show(supportFragmentManager, "")
                true
            }
            R.id.refresh_list -> {
                // refresh list
                bulkSmsViewModel.initBulkMessages()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    override fun onButtonClicked(purpose: Int, button: String) {
        when (purpose) {
            SEND_MESSAGE_PURPOSE -> {
                if (button == "yes") {
                    if (!bulkSmsViewModel.getBulkMessages().value.isNullOrEmpty()) {
                        // send bulk messages
                        CoroutineScope(Dispatchers.Main).launch {

                            val res =
                                MessageHandler.sendBulkMessages(bulkSmsViewModel.getBulkMessages().value!!.toList())

                            if (res) {
                                Quicktoast(this@BulkSms).sinfo("Mesazhet u dërguan")
                            }

                        }
                    } else {
                        Quicktoast(this).swarn("Lista e nxënësve është bosh")
                    }
                }
            }
            else -> {
                Quicktoast(this).swarn("Ndodhi një gabim")
            }
        }
    }

    override fun onBulkMessageSelected(messageItem: BulkMessage) {
        TextDialog(messageItem.message).show(supportFragmentManager, "")
    }
}