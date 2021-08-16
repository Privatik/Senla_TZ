package com.example.senla_tz.util.extends

import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.senla_tz.R
import com.google.android.material.snackbar.Snackbar

fun Fragment.showSnackBar(txt: String){
    val s = Snackbar.make(requireView(), txt, Snackbar.LENGTH_INDEFINITE)
    s.setAction("Ок") {}
    s.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    s.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.purple_500))
    s.show()
}

fun Fragment.showToast(txt: String, short: Boolean = true) {
    if (context != null) {
        Toast.makeText(context, txt, if (short) Toast.LENGTH_SHORT else Toast.LENGTH_LONG)
            .show()
    }
}


