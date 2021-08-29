package com.example.senla_tz.ui.dialog.load

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.senla_tz.R

class LoadDialog: DialogFragment(R.layout.dialog_load) {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }
}