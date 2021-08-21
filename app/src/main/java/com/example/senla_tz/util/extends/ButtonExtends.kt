package com.example.senla_tz.util.extends

import android.animation.ObjectAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatButton
import androidx.core.animation.addListener
import com.example.senla_tz.R

private const val ROTATION_X = "rotationX"
fun AppCompatButton.flipDownAnimation(changeText: String? = null, onChange: () -> Unit){

    val animationStart = ObjectAnimator.ofFloat(this, ROTATION_X , 0f, -90f)
    animationStart.duration = 200
    animationStart.start()

    animationStart.addListener(
        onEnd = {changeText?.let { text = changeText }}
    )

    val animationEnd = ObjectAnimator.ofFloat(this, ROTATION_X , 90f, 0f)
    animationEnd.duration = 200
    animationEnd.startDelay = 200

    animationEnd.addListener(onEnd = {
        onChange()
    })

    animationEnd.start()
}