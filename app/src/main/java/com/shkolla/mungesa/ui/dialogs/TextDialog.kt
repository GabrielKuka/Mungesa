package com.shkolla.mungesa.ui.dialogs


import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.DialogFragment
import com.shkolla.mungesa.R

class TextDialog(private val text: String) :
    DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder =
                AlertDialog.Builder(ContextThemeWrapper(it, android.R.style.Theme_Material_Dialog))
            builder.setMessage(text)
                .setNeutralButton(R.string.done) { _, _ ->

                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}