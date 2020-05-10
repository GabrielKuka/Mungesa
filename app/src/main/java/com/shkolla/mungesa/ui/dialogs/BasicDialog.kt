package com.shkolla.mungesa.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.DialogFragment
import com.shkolla.mungesa.R

class BasicDialog(
    private val purpose: Int = 0,
    private val content: String = "",
    private val interaction: DialogButtonInteraction? = null
) :
    DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder =
                AlertDialog.Builder(ContextThemeWrapper(it, android.R.style.Theme_Material_Dialog))
            builder.setMessage(content)

                .setPositiveButton(
                    R.string.yes
                ) { dialog, id ->
                    // send messages
                    interaction?.onButtonClicked(purpose, "yes")

                }
                .setNegativeButton(
                    R.string.no
                ) { _, _ ->
                    // User cancelled the dialog
                    interaction?.onButtonClicked(purpose, "no")
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    interface DialogButtonInteraction {
        fun onButtonClicked(purpose: Int, button: String)
    }
}