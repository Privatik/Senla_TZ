package com.example.senla_tz.bind_adapter

import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.senla_tz.R
import android.text.Spanned
import android.text.style.UnderlineSpan


@BindingAdapter("app:isLoginState")
fun isLoginState(view: View, isLoginState: Boolean) {
    view.visibility = if (isLoginState) View.GONE else View.VISIBLE
}

@BindingAdapter("app:textSingButton")
fun textSingButton(textView: TextView, isLoginState: Boolean) {
    textView.apply {
        val ss = SpannableString(
            if (isLoginState)   context.getString(R.string.u_register)
            else                context.getString(R.string.u_login))

        ss.setSpan(UnderlineSpan(), 0, ss.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text = ss
    }
}