package com.example.senla_tz.ui.activity.run

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.util.*

object RunUtility {

    fun checkPermission(compatActivity: AppCompatActivity, hasPermission: () -> Unit){
        val activityResultLauncherActivity = compatActivity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK){
                hasPermission()
            }else {
                compatActivity.finish()
            }
        }

        val activityResultLauncherPermission = compatActivity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val isGranted = it.value
                if (!isGranted) {
//                    (Objects.requireNonNull(compatActivity.getSystemService(Context.ACTIVITY_SERVICE)) as ActivityManager)
//                        .clearApplicationUserData()
                    compatActivity.finish()
                    return@registerForActivityResult
                }
            }

            if (!(compatActivity.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager).isProviderEnabled(
                    LocationManager.GPS_PROVIDER)){
                activityResultLauncherActivity.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            } else {
                hasPermission()
            }

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activityResultLauncherPermission.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            )
        } else {
            activityResultLauncherPermission.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            )
        }
    }
}