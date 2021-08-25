package com.example.senla_tz.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.senla_tz.ui.activity.run.Timer

class TimerService: Service() {
    override fun onBind(p0: Intent): IBinder {
        return TimerBinder()
    }
}

class TimerBinder: Binder() {

    private val timer = Timer()
    var milliseconds = 0L

    fun startTimer(update:(Long) -> Unit){
       timer.startTimer {
           milliseconds = it
           update(it)
       }
    }

    fun stopTimer(){
       timer.stopTimer()
    }
}