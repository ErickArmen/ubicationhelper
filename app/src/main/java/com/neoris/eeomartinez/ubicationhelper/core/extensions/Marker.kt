package com.neoris.eeomartinez.ubicationhelper.core.extensions

import android.os.Handler
import android.os.SystemClock
import android.view.animation.LinearInterpolator
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.neoris.eeomartinez.ubicationhelper.features.map.domain.models.Location

fun Marker.animate(googleMap: GoogleMap, toPosition: Location?){

    val handler = Handler()
    val start = SystemClock.uptimeMillis()
    val proj = googleMap.projection
    val startPoint = proj.toScreenLocation(this.position)
    val startLatLng = proj.fromScreenLocation(startPoint)
    val duration = 500
    val interpolator = LinearInterpolator()

    if (toPosition == null)
        return

    handler.post (object : Runnable{
        override fun run() {

            val elapsed = (SystemClock.uptimeMillis() - start).toFloat()
            val t = interpolator.getInterpolation(elapsed / duration)
            val lat = t * toPosition.lat + (1 - t) * startLatLng.latitude
            val lon = t * toPosition.lon + (1 - t) * startLatLng.longitude
            this@animate.position = LatLng(lat, lon)

            if (t < 1){
                handler.postDelayed(this, 16)
            }
        }
    })
    this.rotation = toPosition.bearing
}