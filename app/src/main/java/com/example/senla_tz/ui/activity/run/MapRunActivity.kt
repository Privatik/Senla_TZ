package com.example.senla_tz.ui.activity.run

import androidx.core.content.ContextCompat
import com.example.senla_tz.R
import com.example.senla_tz.base.BaseActivity
import com.example.senla_tz.util.extends.bitmapDescriptorFromVector
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

private const val ZOOM_CAMERA = 17f
abstract class MapRunActivity: BaseActivity(), OnMapReadyCallback {

    protected var mMap: GoogleMap? = null

    protected val line : PolylineOptions by lazy {
        PolylineOptions().apply {
            width(5f)
            color(ContextCompat.getColor(this@MapRunActivity, R.color.purple_700))
        }
    }

    protected var currentMarker: Marker? = null

    protected fun finishLine(list: List<LatLng>, isDrawLine: Boolean = false){
        if (isDrawLine){
            line.points.clear()
            line.addAll(list)

            mMap?.addPolyline(line)
        }

        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(list.last(), ZOOM_CAMERA))

        mMap?.addMarker(MarkerOptions()
            .title(getString(R.string.start))
            .position(list.first())
            .icon(bitmapDescriptorFromVector(R.drawable.point_start))
        )

        mMap?.addMarker(MarkerOptions()
            .title(getString(R.string.finish))
            .position(list.last())
            .icon(bitmapDescriptorFromVector(R.drawable.point_finish))
        )
    }

    protected fun updateMyLocate(latLng: LatLng){
        if (currentMarker == null) {
            mMap?.addMarker(
                MarkerOptions()
                .title(getString(R.string.me))
                .position(latLng)
                .icon(bitmapDescriptorFromVector(R.drawable.point_me))
            )?.let { marker ->
                currentMarker = marker
            }
            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_CAMERA))
        } else {
            currentMarker?.position = latLng
            mMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        }
    }

    protected open fun updateTrack(list: List<LatLng>){
        line.points.clear()
        line.addAll(list)

        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(list.last(), ZOOM_CAMERA))
        mMap?.addPolyline(line)
    }

    override fun onDestroy() {
        mMap = null
        super.onDestroy()
    }
}