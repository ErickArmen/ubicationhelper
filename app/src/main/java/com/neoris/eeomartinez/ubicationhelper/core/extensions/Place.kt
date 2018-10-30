package com.neoris.eeomartinez.ubicationhelper.core.extensions

import android.location.Location.distanceBetween
import com.google.android.gms.location.places.Place
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import kotlin.math.roundToInt

fun Place.zoomBetweenPlaces(finalPlace: Place, mMap: GoogleMap){

    val middlePoint = LatLng((this.latLng.latitude + finalPlace.latLng.latitude) / 2,
            (this.latLng.longitude + finalPlace.latLng.longitude) / 2)

    val distance = FloatArray(1)
    distanceBetween(this.latLng.latitude, this.latLng.longitude,
            finalPlace.latLng.latitude, finalPlace.latLng.longitude, distance)

    when (distance[0].roundToInt()) {
        in 0..60 -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middlePoint, 20f))
        in 60..255 -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middlePoint, 18f))
        in 255..1000 -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middlePoint, 16f))
        in 1000..4000 -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middlePoint, 14f))
        in 4000..16500 -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middlePoint, 12f))
        in 16500..65000 -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middlePoint, 10f))
        in 65000..260000 -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middlePoint, 8f))
        in 260000..1000000 -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middlePoint, 6f))
        else -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middlePoint, 4f))
    }
}