package com.example.senla_tz.util.extends

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.senla_tz.R
import com.example.senla_tz.base.BaseActivity
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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

fun Fragment.isLoadState(): Boolean{
    if (requireActivity() is BaseActivity){
        return (requireActivity() as BaseActivity).isLoadState()
    }
    return false
}


fun AppCompatActivity.showToast(txt: String, short: Boolean = true) {
    Toast.makeText(this, txt, if (short) Toast.LENGTH_SHORT else Toast.LENGTH_LONG)
        .show()
}
fun AppCompatActivity.showSnackBar(view: View, txt: String){
    val s = Snackbar.make(view, txt, Snackbar.LENGTH_INDEFINITE)
    s.setAction("Ок") {}
    s.setTextColor(ContextCompat.getColor(this, R.color.white))
    s.setBackgroundTint(ContextCompat.getColor(this, R.color.purple_500))
    s.show()
}


fun View.setVisible(isVisible:Boolean){
    visibility =when (isVisible){
        true -> View.VISIBLE
        false ->View.GONE
    }
}

fun View.getVisible() = visibility == View.VISIBLE


fun BaseActivity.bitmapDescriptorFromVector(
    @DrawableRes vectorDrawableResourceId: Int
): BitmapDescriptor {
    val background = ContextCompat.getDrawable(this, vectorDrawableResourceId)
    background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)

    val bitmap = Bitmap.createBitmap(
        background.intrinsicWidth,
        background.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    background.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}