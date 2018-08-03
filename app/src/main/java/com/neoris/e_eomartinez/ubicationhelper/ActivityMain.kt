package com.neoris.e_eomartinez.ubicationhelper

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.MenuItem

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*

class ActivityMain : AppCompatActivity(), OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {


    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mainNaviView.itemIconTintList = null
        mainNaviView.setNavigationItemSelectedListener(this)
        val st = "";
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val neoris = LatLng(25.699820, -100.261592)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(25.699507, -100.259), 16f))
        mMap.addMarker(MarkerOptions().position(neoris).title("Bus Station").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_bus)))
        mMap.addMarker(MarkerOptions().position(LatLng(25.698283, -100.260168)).title("Coffee Shop").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_coffee)))
        mMap.addMarker(MarkerOptions().position(LatLng(25.700195, -100.256913)).title("Car Wash").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car)))
        mMap.addMarker(MarkerOptions().position(LatLng(25.698874, -100.257411)).title("Church").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_church)))
        mMap.addMarker(MarkerOptions().position(LatLng(25.700680, -100.259364)).title("Park").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_park)))
        mMap.addMarker(MarkerOptions().position(LatLng(25.701658, -100.258504)).title("Restaurant").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_restaurant)))
        mMap.addMarker(MarkerOptions().position(LatLng(25.699507, -100.259549)).title("Shop").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_shop)))
    }

    private fun goToTimeLine(title: String){
        val intent = Intent(this, ActivityTimeLine::class.java)
        intent.putExtra("title", title)
        startActivity(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val x = item.order
        when(x){

            0 -> {
                goToTimeLine(item.title.toString())
            }
        }

        mainDrawer.closeDrawer(GravityCompat.START)
        return true
    }
}
