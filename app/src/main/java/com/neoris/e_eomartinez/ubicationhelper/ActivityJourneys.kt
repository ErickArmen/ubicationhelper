package com.neoris.e_eomartinez.ubicationhelper

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
import com.google.maps.android.ui.IconGenerator
import kotlinx.android.synthetic.main.custom_marker.view.*


class ActivityJourneys: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var journey: String

    val busStation = LatLng(25.675742, -100.416814)
    val coffeShop = LatLng(25.676742, -100.419)
    val carWash = LatLng(25.677417, -100.423382)
    val church = LatLng(25.673742, -100.422)
    val park = LatLng(25.672002, -100.423918)
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

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(25.675655, -100.420106), 16f))

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

        val bubbleFactory = IconGenerator(this)

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val myView = inflater.inflate(R.layout.custom_marker, null, false)
        bubbleFactory.setContentView(myView)
        bubbleFactory.setBackground(ColorDrawable(Color.TRANSPARENT))

        //bubbleFactory.setBackground(ContextCompat.getDrawable(this, R.mipmap.ic_bus))
        //bubbleFactory.setTextAppearance(this, R.style.iconGenText)
        //val zeroIcon = bubbleFactory.makeIcon("0")


        when(journey){
            "Bus Station" -> {


               /* val bitmapdraw = ContextCompat.getDrawable(this, R.mipmap.ic_zero) as BitmapDrawable
                val b = bitmapdraw.bitmap
                val smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false)*/

                myView.img_marker.setImageResource(R.mipmap.ic_bus)
                myView.tv_marker_text.text = "0"
                mMap.addMarker(MarkerOptions()
                        .position(busStation).title("Bus Station")
                        .icon(BitmapDescriptorFactory.fromBitmap(bubbleFactory.makeIcon())))

                myView.img_marker.setImageResource(R.mipmap.ic_coffee)
                myView.tv_marker_text.text = "1"
                mMap.addMarker(MarkerOptions()
                        .position(coffeShop).title("Coffee Shop")
                        .icon(BitmapDescriptorFactory.fromBitmap(bubbleFactory.makeIcon())))

                /*mMap.addMarker(MarkerOptions()
                        .position(coffeShop)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_looks_one_black_24dp)))*/

                myView.img_marker.setImageResource(R.mipmap.ic_car)
                myView.tv_marker_text.text = "2"
                mMap.addMarker(MarkerOptions()
                        .position(carWash).title("Car Wash")
                        .icon(BitmapDescriptorFactory.fromBitmap(bubbleFactory.makeIcon())))

                myView.img_marker.setImageResource(R.mipmap.ic_church)
                myView.tv_marker_text.text = "3"
                mMap.addMarker(MarkerOptions()
                        .position(church).title("Church")
                        .icon(BitmapDescriptorFactory.fromBitmap(bubbleFactory.makeIcon())))

                myView.img_marker.setImageResource(R.mipmap.ic_park)
                myView.tv_marker_text.text = "4"
                mMap.addMarker(MarkerOptions()
                        .position(park).title("Park")
                        .icon(BitmapDescriptorFactory.fromBitmap(bubbleFactory.makeIcon())))

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