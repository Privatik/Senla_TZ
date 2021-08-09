package com.example.senla_tz.bind_adapter

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.senla_tz.R

@BindingAdapter("app:isLoginState")
fun isLoginState(view: View, isLoginState: Boolean) {
    view.visibility = if (isLoginState) View.GONE else View.VISIBLE
}

@BindingAdapter("app:textSingButton")
fun textSingButton(textView: TextView, isLoginState: Boolean) {
    textView.apply {
        text = if (isLoginState) context.getString(R.string.u_register) else context.getString(R.string.u_login)
    }
}