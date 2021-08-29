package com.example.senla_tz.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.senla_tz.ui.dialog.load.LoadDialog

private const val LOAD = "load"
open class BaseActivity: AppCompatActivity() {

    private var dialogLoad: DialogFragment? = null

    fun openLoadDialog(){
        dialogLoad = LoadDialog().also {
            it.isCancelable = false
            it.show(supportFragmentManager.beginTransaction(), LOAD)
        }
    }

    fun closeLoadDialog(){
        dialogLoad?.dismiss()
        dialogLoad = null
    }

    fun isLoadState() = dialogLoad != null

    override fun onDestroy() {
        closeLoadDialog()
        super.onDestroy()
    }
}