package com.neoris.e_eomartinez.ubicationhelper.features.map.presentation

import android.os.Handler
import android.os.SystemClock
import android.view.animation.LinearInterpolator
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

class MarkerAnimator(val googleMap: GoogleMap) {

    fun animateMarker(marker: Marker?, toPosition: LatLng) {

        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val proj = googleMap.projection
        val startPoint = proj.toScreenLocation(marker?.position)
        val startLatLng = proj.fromScreenLocation(startPoint)
        val duration = 500
        val interpolator = LinearInterpolator()

        handler.post (object : Runnable{
            override fun run() {

                val elapsed = (SystemClock.uptimeMillis() - start).toFloat()
                val t = interpolator.getInterpolation(elapsed / duration)
                val lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude
                val lon = t * toPosition.longitude + (1 - t) * startLatLng.longitude
                marker?.position = LatLng(lat, lon)

                if (t < 1){
                    handler.postDelayed(this, 16)
                }
            }
        })
    }
}