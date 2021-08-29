package com.example.senla_tz.bind_adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.senla_tz.util.extends.returnTimeFromMilliseconds


@BindingAdapter("app:textTimer")
fun textTimer(textView: TextView, time: Long) {
    textView.text = time.returnTimeFromMilliseconds()
}


@SuppressLint("SetTextI18n")
@BindingAdapter("app:textDistance")
fun textDistance(textView: TextView, distance: Long) {
    textView.text = "$distance м"
}