package com.example.senla_tz.util.extends

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.senla_tz.R
import com.example.senla_tz.base.BaseActivity
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

fun Fragment.openLoadDialog(){
    if (requireActivity() is BaseActivity){
        (requireActivity() as BaseActivity).openLoadDialog()
    }
}

fun Fragment.closeLoadDialog(){
    if (requireActivity() is BaseActivity){
        (requireActivity() as BaseActivity).closeLoadDialog()
    }
}


fun AppCompatActivity.showToast(txt: String, short: Boolean = true) {
    Toast.makeText(this, txt, if (short) Toast.LENGTH_SHORT else Toast.LENGTH_LONG)
        .show()
}

fun View.setVisible(isVisible:Boolean){
    visibility =when (isVisible){
        true -> View.VISIBLE
        false ->View.GONE
    }
}

fun View.getVisible() = visibility == View.VISIBLE


