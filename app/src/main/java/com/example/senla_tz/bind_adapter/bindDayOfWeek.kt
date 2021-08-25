package com.example.senla_tz.bind_adapter

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter

@BindingAdapter("app:isSelect")
fun isSelect(button: AppCompatButton, isSelect: Boolean) {
    button.isSelected = isSelect
}