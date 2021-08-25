package com.example.senla_tz.ui.activity.run

import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import java.util.*
import java.util.Timer
import kotlin.system.measureTimeMillis

class Timer() {

    private val timer = Timer()

    fun startTimer(update: (Long) -> Unit) {
        var milliseconds = 0L

        timer.schedule(object : TimerTask(){
            override fun run() {
                milliseconds += 12
                update(milliseconds)
            }

        },0,10)

    }

    fun stopTimer()  {
        timer.cancel()
    }

}