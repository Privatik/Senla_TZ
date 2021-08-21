package com.example.senla_tz.service

import android.app.Service
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

private val TAG = RunService::class.java.simpleName
class RunService: Service() {


    override fun onBind(intent: Intent): IBinder {
        return LocationBinder()
    }

    class LocationBinder: Binder() {
        private lateinit var changeLocation: ChangeLatLng

        val listener = object : LocationListener {
            override fun onLocationChanged(it: Location) {
                val latLng = LatLng(it.latitude, it.longitude)

                Log.e(TAG,"locate - $latLng")
                changeLocation.update(latLng)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) = Unit
        }

        fun updateLocationListener(changeLocation: ChangeLatLng){
            this.changeLocation = changeLocation
        }

        interface ChangeLatLng{
            fun update(latLng: LatLng)
        }
    }
}