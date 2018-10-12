package com.neoris.e_eomartinez.ubicationhelper.features.journeys.presentation.activities

import android.content.Context
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
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import com.google.maps.android.ui.IconGenerator
import com.neoris.e_eomartinez.ubicationhelper.R
import kotlinx.android.synthetic.main.custom_marker.view.*


class ActivityJourneys: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var journey: String
    private lateinit var myView : View
    private lateinit var bubbleFactory: IconGenerator
    private val initialCoord = LatLng(25.675655, -100.420106)
    private val initialZoom = 16f
    private val busStationString = "Bus Station"
    private val coffeShopString = "Coffee Shop"
    private val carWashString = "Car Wash"
    private val churchString = "Church"
    private val parkString = "Park"

    private val busStation = LatLng(25.675742, -100.416814)
    private val coffeShop = LatLng(25.676742, -100.419)
    private val carWash = LatLng(25.677417, -100.423382)
    private val church = LatLng(25.673742, -100.422)
    private val park = LatLng(25.672002, -100.423918)
    //val restaurant = LatLng(25.681, -100.414)
    //val shop = LatLng(25.670742, -100.410)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journeys)

        getIntents()
        setBubbleFactory()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialCoord, initialZoom))
        computeJourney(journey)
    }

    private fun getIntents(){
        journey = intent.getStringExtra(journey_extra)
    }

    private fun setBubbleFactory(){
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        myView = inflater.inflate(R.layout.custom_marker, null, false)
        bubbleFactory = IconGenerator(this)
        bubbleFactory.apply {
            setContentView(myView)
            setBackground(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun computeJourney(journey: String) {

        when(journey){
            busStationString -> {
                setListOfPlaces(arrayListOf(busStationString, coffeShopString, carWashString, churchString, parkString))
                drawRoute(arrayListOf(busStation, coffeShop, carWash, church, park))
            }
        }
    }

    private fun setListOfPlaces(pointList: List<String>){
        var index = 0
        pointList.forEach {
            when(it){
                busStationString -> setMarker(R.mipmap.ic_bus, index.toString(), busStation, it)
                coffeShopString -> setMarker(R.mipmap.ic_coffee, index.toString(), coffeShop, it)
                carWashString -> setMarker(R.mipmap.ic_car, index.toString(), carWash, it)
                churchString -> setMarker(R.mipmap.ic_church, index.toString(), church, it)
                parkString -> setMarker(R.mipmap.ic_park, index.toString(), park, it)
            }
            ++index
        }
    }

    private fun drawRoute(pointList: List<LatLng>){
        val polyLine = PolylineOptions().width(2f).color(Color.RED)
        pointList.forEach { polyLine.add(it) }
        mMap.addPolyline(polyLine)
    }

    private fun setMarker(resource: Int, text: String, position: LatLng, title: String){
        myView.img_marker.setImageResource(resource)
        myView.tv_marker_text.text = text
        mMap.addMarker(MarkerOptions()
                .position(position).title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(bubbleFactory.makeIcon())))
    }

}