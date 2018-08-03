package com.neoris.e_eomartinez.ubicationhelper

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class ActivityJourneys: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var journey: String

    val busStation = LatLng(25.675742, -100.416814)
    val coffeShop = LatLng(25.676742, -100.419)
    val carWash = LatLng(25.679742, -100.421)
    val church = LatLng(25.673742, -100.422)
    val park = LatLng(25.679, -100.416)
    val restaurant = LatLng(25.681, -100.414)
    val shop = LatLng(25.670742, -100.410)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journeys)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        getIntents()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busStation, 15f))

        /*mMap.addMarker(MarkerOptions().position(busStation).title("Bus Station")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_bus)))

        mMap.addMarker(MarkerOptions().position(coffeShop).title("Coffee Shop")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_coffee)))

        mMap.addMarker(MarkerOptions().position(carWash).title("Car Wash")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car)))

        mMap.addMarker(MarkerOptions().position(church).title("Church")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_church)))

        mMap.addMarker(MarkerOptions().position(park).title("Park")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_park)))

        mMap.addMarker(MarkerOptions().position(restaurant).title("Restaurant")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_restaurant)))

        mMap.addMarker(MarkerOptions().position(shop).title("Shop")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_shop)))*/

        /*mMap.addMarker(MarkerOptions().position(LatLng(neoris.latitude+0.001, neoris.longitude)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_one)))
        mMap.addMarker(MarkerOptions().position(LatLng(25.675742, -100.418)))
        mMap.addMarker(MarkerOptions().position(LatLng(25.670742, -100.420)))
        mMap.addMarker(MarkerOptions().position(LatLng(25.680742, -100.422)))*/

        /*val polyLine = PolylineOptions().width(2f).color(Color.BLUE)
        polyLine.add(busStation)
        polyLine.add(coffeShop)
        polyLine.add(carWash)
        polyLine.add(church)
        polyLine.add(park)
        polyLine.add(restaurant)
        polyLine.add(shop)

        mMap.addPolyline(polyLine)*/

        computeJourney(journey)

    }

    private fun getIntents(){
        journey = intent.getStringExtra("journey")
    }

    private fun computeJourney(journey: String){

        when(journey){
            "Bus Station" -> {

                mMap.addMarker(MarkerOptions()
                        .position(busStation).title("Bus Station")
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_bus)))

                mMap.addMarker(MarkerOptions()
                        .position(coffeShop).title("Coffee Shop")
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_coffee)))

                /*mMap.addMarker(MarkerOptions()
                        .position(coffeShop)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_looks_one_black_24dp)))*/

                mMap.addMarker(MarkerOptions()
                        .position(carWash).title("Car Wash")
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car)))

                mMap.addMarker(MarkerOptions()
                        .position(church).title("Church")
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_church)))

                mMap.addMarker(MarkerOptions()
                        .position(park).title("Park")
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_park)))

                val polyLine = PolylineOptions().width(2f).color(Color.RED)
                polyLine.add(busStation)
                polyLine.add(coffeShop)
                polyLine.add(carWash)
                polyLine.add(church)
                polyLine.add(park)
                mMap.addPolyline(polyLine)

            }
        }
    }
}